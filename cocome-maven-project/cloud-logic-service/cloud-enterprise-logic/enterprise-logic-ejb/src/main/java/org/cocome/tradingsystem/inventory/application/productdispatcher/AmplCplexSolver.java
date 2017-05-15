/***************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.application.productdispatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.store.ProductAmountTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.StoreTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * AMPL/CPLEX based solver for the problem of optimal transportation costs.
 * <p>
 * Required for UC 8 (Optimization part)
 * 
 * @see IOptimizationSolverLocal
 * 
 * @author kelsaka
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@ApplicationScoped
public class AmplCplexSolver implements IOptimizationSolverLocal {

	private static final Logger __log__ =
			Logger.getLogger(AmplCplexSolver.class);

	// AMPL home and data directories

	private final File __AMPL_HOME__ =
			new File(System.getProperty("ampl.home", "..//resources//solver//"));

	private final File __AMPL_DATA__ =
			new File(System.getProperty("ampl.data", "..//resources//solver//"));

	// AMPL/CPLEX executables

	private final File __AMPL_EXE__ =
			new File(__AMPL_HOME__, __executableName("ampl"));

	private final File __CPLEX_EXE__ =
			new File(__AMPL_HOME__, __executableName("cplex"));

	//

	@Inject
	private IStoreQuery __storeQuery;
	
	@Inject
	IStoreDataFactory storeFactory;
	
	@Inject
	IEnterpriseDataFactory enterpriseFactory;


	/*
	 * @see OptimisationSolverIf#solveOptimization(Collection, Map, Map)
	 */
	@Override
	public Map<StoreTO, Collection<ProductAmountTO>> solveOptimization(
			final Collection<ProductAmountTO> requiredProductAmounts,
			final Map<IStore, Collection<IStockItem>> storeStockItems,
			final Map<IStore, Integer> storeDistances
			) {
		//
		// Satisfy solver precondition: only solve the problem for required
		// products that are actually available. If none of the required
		// products is available, an empty solution map is returned.
		//
		final Collection<ProductAmountTO> availableProductAmounts =
				__removeUnavailableProducts(requiredProductAmounts, storeStockItems);

		if (availableProductAmounts.size() <= 0) {
			return Collections.emptyMap();
		}

		//
		// Create problem description and solver input, execute the command
		// line solver and parse its output back into solution map.
		//
		try {
			final String amplCommands = __prepareSolverInput(
					storeStockItems, storeDistances, availableProductAmounts
					);

			final String output = __invokeSolver(amplCommands);
			return __solutionFromSolverOutput(output);

		} catch (final Exception e) {
			//
			// Return empty solution map if any problem occurred when
			// executing the solver.
			//
			__log__.error("Product transport optimization failed", e);
			return Collections.emptyMap();
		}
	}

	/**
	 * Filters the collection of required products to remove products that are
	 * not available in the stores managed by the enterprise.
	 * <p>
	 * This is needed to satisfy the CPLEX solver precondition which requires that a solution must exist for CPLEX to deliver results, because the solver is not able
	 * to provide sub-optimal solutions where not all the required products are available with a sufficient amount.
	 */
	private Collection<ProductAmountTO> __removeUnavailableProducts(
			final Collection<ProductAmountTO> requiredAmounts,
			final Map<IStore, Collection<IStockItem>> storeStocks
			) {
		final Collection<ProductAmountTO> result = new ArrayList<ProductAmountTO>();

		for (final ProductAmountTO requiredAmount : requiredAmounts) {
			//
			// Look for the product in all stores. Stop searching when the
			// product is found -- we only need to know if it is available in
			// ANY of the stores.
			//
			SEARCH_STORES: for (final Collection<IStockItem> storeItems : storeStocks.values()) {
				final long amountAvailable = __findStockAmount(
						storeItems, requiredAmount.getProduct()
						);

				if (amountAvailable > 0) {
					result.add(requiredAmount);
					break SEARCH_STORES;
				}
			}
		}

		return result;
	}

	/**
	 * Prepares the data file for the solver and returns a command string for
	 * the solver which configures it and starts the optimization.
	 */
	private String __prepareSolverInput(
			final Map<IStore, Collection<IStockItem>> storeStockItems,
			final Map<IStore, Integer> storeDistances,
			final Collection<ProductAmountTO> availableProductAmounts
			) {
		final String data = __createDataString(
				availableProductAmounts, storeStockItems, storeDistances
				);

		// predefined model file
		final File modelFile = new File(__AMPL_DATA__, "cocome.mod");

		// create the data input file
		final File dataFile = new File(__AMPL_DATA__, "cocome.dat");
		__createTmpFile(dataFile, data);

		// create command string for AMPL
		return String.format(
				"option solver \"%s\";\n" +
						"model \"%s\";\n" +
						"data \"%s\";\n" +
						"solve;\n" +
						"display {i in PRODUCT, j in STORE}: shipping_amount[i,j];\n" +
						"quit;\n",
				__CPLEX_EXE__, modelFile, dataFile
				);
	}

	/**
	 * Invokes the AMPL/CPLEX solver with given input and returns the output
	 * produced by the solver.
	 * 
	 * @throws InterruptedException
	 */
	private String __invokeSolver(
			final String input
			) throws IOException, InterruptedException {
		final AmplPipe ampl = AmplPipe.open(__AMPL_EXE__);

		try {
			ampl.send(input);
			return ampl.receive();

		} finally {
			ampl.close();
		}
	}

	/**
	 * Parses the solution from solver output and converts it from
	 * the solver domain to the transfer object domain.
	 * @throws NotInDatabaseException 
	 */
	private Map<StoreTO, Collection<ProductAmountTO>> __solutionFromSolverOutput(
			final String solverOutput
			) throws NotInDatabaseException {
		final CplexSolution solution = CplexSolution.parse(solverOutput);
		return __solutionFromSolverDomain(solution);
	}

	/**
	 * Converts the solution from solver domain to transfer object domain. This
	 * requires database access because the solution objects are first converted
	 * to entity objects, which are subsequently converted to transfer objects.
	 * @throws NotInDatabaseException 
	 */
	private Map<StoreTO, Collection<ProductAmountTO>> __solutionFromSolverDomain(
			final CplexSolution solution
			) throws NotInDatabaseException {
		final Map<StoreTO, Collection<ProductAmountTO>> result = new HashMap<StoreTO, Collection<ProductAmountTO>>();

		for (final CplexSolution.Store store : solution.stores()) {
			final StoreTO storeTO = __getStoreTO(store);
			__log__.debug(String.format(
					"Products sent by %s at %s",
					storeTO.getName(), storeTO.getLocation()
					));

			final Collection<ProductAmountTO> productsTOs = __getProductTOs(store.products());

			result.put(storeTO, productsTOs);
		}

		return result;
	}

	private StoreTO __getStoreTO(final CplexSolution.Store store) throws NotInDatabaseException {
		final IStore storeEntity = __storeQuery.queryStoreById(
				store.id());

		return storeFactory.fillStoreTO(storeEntity);
	}

	private Collection<ProductAmountTO> __getProductTOs(
			final Collection<CplexSolution.Product> products
			) throws NotInDatabaseException {
		final Collection<ProductAmountTO> result = new ArrayList<ProductAmountTO>();

		for (final CplexSolution.Product product : products) {
			final ProductTO productTO = __getProductTO(product);
			__log__.debug(String.format(
					"\t%s, barcode %d, amount %d",
					productTO.getName(), productTO.getBarcode(), product.amount()
					));

			final ProductAmountTO productAmountTO =
					__newProductAmountTO(productTO, product.amount());

			result.add(productAmountTO);
		}

		return result;
	}

	private ProductAmountTO __newProductAmountTO(
			final ProductTO productTO, final long amount
			) {
		final ProductAmountTO result = new ProductAmountTO();
		result.setProduct(productTO);
		result.setAmount(amount);
		return result;
	}

	private ProductTO __getProductTO(
			final CplexSolution.Product product
			) throws NotInDatabaseException {
		final IProduct productEntity = __storeQuery.queryProductById(
				product.id());

		return enterpriseFactory.fillProductTO(productEntity);
	}

	private void __createTmpFile(final File file, final String content) {
		try {
			file.deleteOnExit(); // comment out for testing
			final FileWriter out = new FileWriter(file);
			out.write(content);
			out.close();

		} catch (final IOException e) {
			throw new EJBException("Error writing file or data for ampl.", e);
		}
	}

	/**
	 * Creates the AMPL input data string. Typical output:
	 * 
	 * <pre>
	 *         param: STORE: dist :=
	 *             "KA"        20
	 *             "OL"        32
	 *             "FFM"       74
	 *             "M"         30
	 *             "B"         50 ;
	 * 
	 *         param: PRODUCT: amount :=
	 *             "Nutella"   5
	 *             "Snickers"  3 ;
	 * 
	 *      	param stock (tr):
	 *                         "Nutella"   "Snickers" :=
	 *             "KA"        30          2
	 *             "OL"        4           40
	 *             "FFM"       0           10
	 *             "M"         10          0
	 *             "B"         25          7 ;
	 * </pre>
	 */
	private String __createDataString(
			final Collection<ProductAmountTO> requiredProductAmounts,
			final Map<IStore, Collection<IStockItem>> storeStockItems,
			final Map<IStore, Integer> storeDistances
			) {
		final Formatter formatter = new Formatter();

		__appendStoreDistances(storeDistances, formatter);

		__appendRequiredProductAmounts(requiredProductAmounts, formatter);

		__appendAvailableProductAmountsMatrix(
				storeStockItems, requiredProductAmounts, formatter);

		return formatter.toString();
	}

	private void __appendStoreDistances(
			final Map<IStore, Integer> storeDistances,
			final Formatter output
			) {
		output.format("param:\nSTORE:\tdist\t:=\n");

		for (final Entry<IStore, Integer> storeEntry : storeDistances.entrySet()) {
			output.format("\"Store%d\"\t%d\n",
					storeEntry.getKey().getId(), storeEntry.getValue()
					);
		}

		output.format(";\n\n");
	}

	private void __appendRequiredProductAmounts(
			final Collection<ProductAmountTO> productAmounts,
			final Formatter output
			) {
		output.format("param:\nPRODUCT:\tamount\t:=\n");

		for (final ProductAmountTO productAmount : productAmounts) {
			output.format("\"Product%d\"\t%d\n",
					productAmount.getProduct().getId(), productAmount.getAmount()
					);
		}

		output.format(";\n\n");
	}

	private void __appendAvailableProductAmountsMatrix(
			final Map<IStore, Collection<IStockItem>> storeStockItems,
			final Collection<ProductAmountTO> requiredProductAmounts,
			final Formatter output
			) {
		__appendOfferingStoresMatrixHeader(requiredProductAmounts, output);

		//
		// Create matrix row for each store, with columns containing the amount
		// of given product, with the following format:
		//
		// Store<store-id> <amount-for-product-A> <amount-for-product-B>
		//
		for (final Entry<IStore, Collection<IStockItem>> storeEntry : storeStockItems.entrySet()) {
			__appendOfferingStoreMatrixRow(
					storeEntry.getKey(), storeEntry.getValue(),
					requiredProductAmounts, output);
		}

		output.format("\n;\n");
	}

	private void __appendOfferingStoresMatrixHeader(
			final Collection<ProductAmountTO> requiredProductAmounts,
			final Formatter output
			) {
		output.format("param\nstock (tr):");

		for (final ProductAmountTO productAmount : requiredProductAmounts) {
			output.format("\t\"Product%d\"", productAmount.getProduct().getId());
		}

		output.format("\t:=");
	}

	private void __appendOfferingStoreMatrixRow(
			final IStore offeringStore,
			final Collection<IStockItem> stockItems,
			final Collection<ProductAmountTO> requiredProductAmounts,
			final Formatter output
			) {
		//
		// For each of the required products, find the amount available
		// at the offering store.
		//
		output.format("\n\"Store%d\"", offeringStore.getId());
		for (final ProductAmountTO requiredProductAmount : requiredProductAmounts) {
			final long amount = __findStockAmount(
					stockItems, requiredProductAmount.getProduct()
					);

			output.format("\t%d", amount);
		}
	}

	/**
	 * Returns the stock amount of required product available in the given
	 * collection of stock items. Returns the amount of the product in stock, or
	 * zero if the required product is not in the collection of stock items,
	 */
	private long __findStockAmount(
			final Collection<IStockItem> stockItems,
			final ProductTO requiredProduct
			) {
		for (final IStockItem stockItem : stockItems) {
			final IProduct product = stockItem.getProduct();
			final ProductTO productTO = enterpriseFactory.fillProductTO(product);
			if (requiredProduct.equalsProduct(productTO)) {
				return stockItem.getAmount();
			}
		}

		return 0;
	}

	private String __executableName(final String baseName) {
		final boolean runningOnWindows =
				System.getProperty("os.name").toLowerCase().contains("Windows");

		return runningOnWindows ? baseName + ".exe" : baseName;
	}

	//
	// TODO Test for solution parser.
	//

//	public static void main(final String[] args) {
//		final CplexSolution solution = CplexSolution.parse(__testString());
//
//		System.out.printf(
//				"Solution comprises %d stores\n",
//				solution.stores().size()
//				);
//
//		for (final CplexSolution.Store store : solution.stores()) {
//			final Collection<CplexSolution.Product> products = store.products();
//
//			System.out.printf(
//					"Store %d ships %d products\n",
//					store.id(), products.size()
//					);
//
//			for (final CplexSolution.Product product : products) {
//				System.out.printf(
//						"\tproduct %d, amount %d\n",
//						product.id(), product.amount()
//						);
//			}
//		}
//	}
//
//	private static String __testString() {
//		//
//		// Sample output data from CPLEX solver.
//		//
//		return
//		"CPLEX 10.1.0: optimal integer solution; objective 0\n" +
//				"0 MIP simplex iterations\n" +
//				"0 branch-and-bound nodes\n" +
//				"shipping_amount['Product98404','Store65537'] = 100\n" +
//				"shipping_amount['Product98404','Store65538'] = 0\n" +
//				"shipping_amount['Product98404','Store65536'] = 0\n" +
//				"shipping_amount['Product229476','Store65537'] = 100\n" +
//				"shipping_amount['Product229476','Store65538'] = 0\n" +
//				"shipping_amount['Product229476','Store65536'] = 0\n" +
//				"shipping_amount['Product360548','Store65537'] = 100\n" +
//				"shipping_amount['Product360548','Store65538'] = 0\n" +
//				"shipping_amount['Product360548','Store65536'] = 0\n" +
//				"shipping_amount['Product491620','Store65537'] = 100\n" +
//				"shipping_amount['Product491620','Store65538'] = 0\n" +
//				"shipping_amount['Product491620','Store65536'] = 0\n" +
//				"shipping_amount['Product622692','Store65537'] = 100\n" +
//				"shipping_amount['Product622692','Store65538'] = 0\n" +
//				"shipping_amount['Product622692','Store65536'] = 0\n" +
//				"shipping_amount['Product753764','Store65537'] = 100\n" +
//				"shipping_amount['Product753764','Store65538'] = 0\n" +
//				"shipping_amount['Product753764','Store65536'] = 0\n" +
//				"shipping_amount['Product884836','Store65537'] = 100\n" +
//				"shipping_amount['Product884836','Store65538'] = 0\n" +
//				"shipping_amount['Product884836','Store65536'] = 0\n" +
//				"shipping_amount['Product1015908','Store65537'] = 100\n" +
//				"shipping_amount['Product1015908','Store65538'] = 0\n" +
//				"shipping_amount['Product1015908','Store65536'] = 0\n" +
//				"shipping_amount['Product1146980','Store65537'] = 100\n" +
//				"shipping_amount['Product1146980','Store65538'] = 0\n" +
//				"shipping_amount['Product1146980','Store65536'] = 0\n" +
//				"shipping_amount['Product1278052','Store65537'] = 100\n" +
//				"shipping_amount['Product1278052','Store65538'] = 0\n" +
//				"shipping_amount['Product1278052','Store65536'] = 0\n" +
//				"shipping_amount['Product1507428','Store65537'] = 100\n" +
//				"shipping_amount['Product1507428','Store65538'] = 0\n" +
//				"shipping_amount['Product1507428','Store65536'] = 0\n" +
//				"\n";
//	}

}
