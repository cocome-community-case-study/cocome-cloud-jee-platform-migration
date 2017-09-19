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

package org.cocome.tradingsystem.inventory.application.reporting;

import java.util.Collection;
import java.util.Formatter;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Implements the {@link IReportingLocal} interface used by the reporting console to
 * realize UC 5 and UC 6.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Stateless
public class ReportingServer implements IReportingLocal {

	//

	@EJB
	private IStoreQuery storeQuery;

	@EJB
	private IEnterpriseQuery enterpriseQuery;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTO getEnterpriseDeliveryReport(long enterpriseId) throws NotInDatabaseException {
		final ITradingEnterprise enterprise = enterpriseQuery.queryEnterpriseById(enterpriseId);

		//

		final Formatter report = new Formatter();
		appendReportHeader(report);
		appendDeliveryReport(enterprise, report);
		appendReportFooter(report);

		//

		return createReportTO(report);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTO getStoreStockReport(long storeId) throws NotInDatabaseException {
		final IStore store = storeQuery.queryStoreById(storeId);

		//

		final Formatter report = new Formatter();
		appendReportHeader(report);
		appendStoreReport(store, report);
		appendReportFooter(report);

		//

		return createReportTO(report);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTO getEnterpriseStockReport(long enterpriseId) throws NotInDatabaseException {
		ITradingEnterprise enterprise = enterpriseQuery.queryEnterpriseById(enterpriseId);

		//

		Formatter report = new Formatter();
		appendReportHeader(report);
		appendEnterpriseReport(enterprise, report);
		appendReportFooter(report);

		//

		return createReportTO(report);
	}

	//

	private void appendDeliveryReport(ITradingEnterprise enterprise,
			Formatter output) throws NotInDatabaseException {
		this.appendTableHeader(output, "ThrowingSupplier ID", "ThrowingSupplier Name",
				"Mean Time To Delivery");

		for (IProductSupplier supplier : enterpriseQuery.querySuppliers(enterprise.getId())) {
			final long mtd = this.enterpriseQuery.getMeanTimeToDelivery(
					supplier, enterprise);

			this.appendTableRow(output, supplier.getId(), supplier.getName(),
					(mtd != 0) ? mtd : "N/A"); // NOCS
		}

		this.appendTableFooter(output);
	}

	private void appendStoreReport(final IStore store, final Formatter output) {
		output.format("<h3>Report for %s at %s, id %d</h3>\n", store.getName(),
				store.getLocation(), store.getId());

		this.appendTableHeader(output, "StockItem ID", "Product Name",
				"Amount", "Min Stock", "Max Stock");

		//

		final Collection<IStockItem> stockItems = this.storeQuery
				.queryAllStockItems(store.getId());

		for (final IStockItem si : stockItems) {
			this.appendTableRow(output, si.getId(), si.getProduct().getName(),
					si.getAmount(), si.getMinStock(), si.getMaxStock());
		}

		this.appendTableFooter(output);
	}

	private void appendEnterpriseReport(final ITradingEnterprise enterprise,
			final Formatter output) {
		output.format("<h2>Stock report for %s</h2>\n", enterprise.getName());

		for (final IStore store : enterprise.getStores()) {
			this.appendStoreReport(store, output);
		}
	}

	//

	private ReportTO createReportTO(final Formatter report) {
		final ReportTO result = new ReportTO();
		result.setReportText(report.toString());
		return result;
	}

	private Formatter appendReportFooter(final Formatter output) {
		return output.format("</body></html>\n");
	}

	private Formatter appendReportHeader(final Formatter output) {
		return output.format("<html><body>\n");
	}

	private void appendTableHeader(final Formatter output,
			final String... names) {
		output.format("<table>\n<tr>");
		for (final String name : names) {
			output.format("<th>%s</th>", name);
		}
		output.format("</tr>\n");
	}

	private void appendTableRow(final Formatter output, final Object... values) {
		output.format("<tr>");
		for (final Object value : values) {
			output.format("<td>%s</td>", value);
		}
		output.format("</tr>\n");
	}

	private void appendTableFooter(final Formatter output) {
		output.format("</table><br/>\n");
	}

}
