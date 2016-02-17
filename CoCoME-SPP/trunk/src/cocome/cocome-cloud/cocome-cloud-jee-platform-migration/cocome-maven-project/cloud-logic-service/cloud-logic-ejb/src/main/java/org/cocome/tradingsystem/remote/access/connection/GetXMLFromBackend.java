package org.cocome.tradingsystem.remote.access.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.remote.access.parsing.MessageToCSV;



public class GetXMLFromBackend {
	
	@Inject
	private String backendHost;
	
	@Inject
	private String backendPort;
	
	@Inject
	private String backendGetDataURL;
	
	private static final Logger LOG = Logger.getLogger(GetXMLFromBackend.class);
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getXMLFromBackend(String urlString) {

		try {

			// make Connection to Back-End to Get XML-File
			URL url = new URL(urlString);
			LOG.debug("Opening URL: " + urlString);
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.addRequestProperty("Content-Type", "application/csv");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String inputLine;

			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {

				response.append(inputLine);
				response.append(System.getProperty("line.separator"));
			}

			in.close();
			
			this.message = response.toString();
			return this.message;

		} catch (MalformedURLException ex) {
			LOG.error("Encountered MalformedURLException: " + ex.toString() +
					"\nCause: " + (ex.getCause() == null ? "No throwable cause" : ex.getCause().toString()) + 
					"\nTrace: ");
			ex.printStackTrace();
			return "MalformedURLException";
		} catch (IOException ex) {
			LOG.error("Encountered IOException: " + ex.toString() + 
					"\nCause: " + (ex.getCause() == null ? "No throwable cause" : ex.getCause().toString()) +
					"\nTrace: ");
			ex.printStackTrace();
			return "IOExceptions";
		}

	}
	
	private String getURLToBackend() {
		String url = "http://" + backendHost + ":" + backendPort + backendGetDataURL;
		LOG.debug("URL to get data from Backend: " + url);
		return url;
	}
	
	public String getProducts(String cond) {

		MessageToCSV csv = new MessageToCSV(
				getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=Product;Product." + cond));
		return csv.getCSV();
	}

	public String getEnterprises(String cond) {
		MessageToCSV csv = new MessageToCSV(
				getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=TradingEnterprise;TradingEnterprise." + cond));
		return csv.getCSV();
	}

	public String getStores(String cond) {
		MessageToCSV csv = new MessageToCSV(
				getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=Store;Store." + cond));
		return csv.getCSV();
	}

	public String getStockItems(String cond) {
		MessageToCSV csv = new MessageToCSV(
				getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=StockItem;StockItem." + cond ));
		return csv.getCSV();
	}

	public String getProductOrder(String cond) {
		MessageToCSV csv = new MessageToCSV(
				getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=ProductOrder;ProductOrder." + cond));
		return csv.getCSV();
	}
	
	public String getProductSupplier(String cond) {
		MessageToCSV csv = new MessageToCSV(
				getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=ProductSupplier;ProductSupplier." + cond));
		return csv.getCSV();
	}
	
	public String getEntity(String entity, String cond) {
		MessageToCSV csv = new MessageToCSV(
				getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=" + entity + ";" + cond));
		return csv.getCSV();
	}
}