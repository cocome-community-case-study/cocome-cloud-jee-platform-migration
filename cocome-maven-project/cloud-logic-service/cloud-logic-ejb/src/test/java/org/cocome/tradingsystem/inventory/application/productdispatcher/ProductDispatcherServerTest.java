package org.cocome.tradingsystem.inventory.application.productdispatcher;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.cocome.tradingsystem.inventory.application.store.FillTransferObjects;
import org.cocome.tradingsystem.inventory.application.store.IStoreInventoryManagerLocal;
import org.cocome.tradingsystem.inventory.application.store.ProductAmountTO;
import org.cocome.tradingsystem.inventory.application.store.ProductMovementTO;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;
import org.cocome.tradingsystem.inventory.application.store.StoreTO;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.store.IStoreQueryLocal;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDispatcherServerTest {
	@Mock
	private IStoreQueryLocal storeQuery;
	
	@Mock
	private IStoreInventoryManagerLocal sourceStore;
	
	@Mock
	private IOptimizationSolverLocal solver;
	
	private static Collection<ProductAmountTO> requiredProducts; 
	
	private static ProductAmountTO req1;
	private static ProductTO prod1;
	
	private static Collection<StockItem> stockItems;
	
	private static StockItem stock;
	
	private static Store source;
	private static Store target;
	
	private static TradingEnterprise enterprise;
	
	private static ProductDispatcherServer dispatcher;
	
	private static Map<StoreTO, Collection<ProductAmountTO>> productMovements;
	
	private static Collection<ProductMovementTO> movementTOs;
	
	private static ProductMovementTO movement;
	
	@BeforeClass
	public static void setUpClass() {		
		prod1 = new ProductTO();
		prod1.setBarcode(1234);
		prod1.setId(1);
		prod1.setName("Test-Product");
		prod1.setPurchasePrice(10.0);
		
		req1 = new ProductAmountTO();
		req1.setAmount(1);
		req1.setProduct(prod1);
		
		requiredProducts = new LinkedList<ProductAmountTO>();
		requiredProducts.add(req1);
		
		stockItems = new LinkedList<StockItem>();
		
		stock = new StockItem();
		
		stockItems.add(stock);
		
		source = new Store();
		target = new Store();
		
		enterprise = new TradingEnterprise();
		enterprise.setId(1);
		
		source.setEnterprise(enterprise);
		target.setEnterprise(enterprise);
		
		source.setId(1);
		target.setId(2);
		
		source.setLocation("Wonderland");
		target.setLocation("Tahiti");
		
		source.setName("sourceStore");
		target.setName("targetStore");
		
		source.setStockItems(stockItems);
		
		Collection<Store> stores = new LinkedList<Store>();
		stores.add(source);
		stores.add(target);
		
		enterprise.setStores(stores);
		
		productMovements = new HashMap<StoreTO, Collection<ProductAmountTO>>();
		productMovements.put(FillTransferObjects.fillStoreTO(source), requiredProducts);
		
		movement = new ProductMovementTO();
		movement.setDestinationStore(FillTransferObjects.fillStoreTO(target));
		movement.setOriginStore(FillTransferObjects.fillStoreTO(source));
		movement.setProductAmounts(requiredProducts);
		
		movementTOs = new LinkedList<ProductMovementTO>();
		movementTOs.add(movement);
	}

	@Before
	public void setUp() throws Exception {
		dispatcher = new ProductDispatcherServer();
		dispatcher.solver = solver;
		dispatcher.sourceStore = sourceStore;
		dispatcher.storeQuery = storeQuery;
		
		when(storeQuery.queryStoreById(1)).thenReturn(source);
		when(storeQuery.queryStoreById(2)).thenReturn(target);
		when(storeQuery.queryStockItemsByProductId(eq(source.getId()), any(long[].class))).thenReturn(stockItems);
		
		when(solver.solveOptimization(eq(requiredProducts), anyMap(), anyMap())).thenReturn(productMovements);
	}

	@Test
	public void testDispatchProductsFromOtherStores() throws NotInDatabaseException, ProductOutOfStockException, UpdateException {
		ProductAmountTO[] products = dispatcher.dispatchProductsFromOtherStores(2, requiredProducts);
		
		verify(sourceStore).markProductsUnavailableInStock(eq(source.getId()), any(ProductMovementTO.class));
		
		assertEquals(1, products.length);
		assertEquals(req1.getAmount(), products[0].getAmount());
		assertEquals(req1.getProduct().getBarcode(), products[0].getProduct().getBarcode());
		assertEquals(req1.getProduct().getId(), products[0].getProduct().getId());
		assertEquals(req1.getProduct().getPurchasePrice(), products[0].getProduct().getPurchasePrice(), 0.1);
		assertEquals(req1.getProduct().getName(), products[0].getProduct().getName());
	}

}
