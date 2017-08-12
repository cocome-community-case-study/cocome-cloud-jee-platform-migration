package org.cocome.tradingsystem.remote.access.connection;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.remote.access.parsing.MessageToCSV;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


@Dependent
public class GetXMLFromBackend implements IBackendQuery {

    @Inject
    private String backendHost;

    @Inject
    private String backendPort;

    @Inject
    private String backendGetDataURL;

    private static final Logger LOG = Logger.getLogger(GetXMLFromBackend.class);

    private String message;

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getXMLFromBackend(String urlString) {
        String note = "init";
        try {

            // make Connection to Back-End to Get XML-File
            URL url = new URL(urlString);
            note = "url";
            LOG.info("Opening URL: " + urlString);
            URLConnection conn = url.openConnection();
            note = "connected " + conn.toString();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            note = "prerequest";
            conn.addRequestProperty("Content-Type", "application/csv");
            note = "added requested properties " + url + " " + conn;
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            note = "buffer def " + in.toString();
            String inputLine;

            StringBuffer response = new StringBuffer();
            note = "string buf " + response.toString();
            while ((inputLine = in.readLine()) != null) {

                response.append(inputLine);
                response.append(System.getProperty("line.separator"));
            }
            note = "copied data";
            in.close();
            note = "input closed";
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
                    "\nTrace: " + " X " + note);
            ex.printStackTrace();
            return "IOExceptions";
        }

    }

    private String getURLToBackend() {
        String url = "http://" + backendHost + ":" + backendPort + backendGetDataURL;
        LOG.info("URL to get data from Backend: " + url);
        return url;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getProducts(java.lang.String)
     */
    @Override
    public String getProducts(String cond) {

        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=Product;Product." + cond));
        return csv.getCSV();
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getEnterprises(java.lang.String)
     */
    @Override
    public String getEnterprises(String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=TradingEnterprise;TradingEnterprise." + cond));
        return csv.getCSV();
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getStores(java.lang.String)
     */
    @Override
    public String getStores(String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=Store;Store." + cond));
        return csv.getCSV();
    }

    @Override
    public String getPlants(String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=Plant;Plant." + cond));
        return csv.getCSV();
    }

        /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getStockItems(java.lang.String)
     */
    @Override
    public String getStockItems(String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=StockItem;StockItem." + cond));
        return csv.getCSV();
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getProductOrder(java.lang.String)
     */
    @Override
    public String getProductOrder(String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=ProductOrder;ProductOrder." + cond));
        return csv.getCSV();
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getProductSupplier(java.lang.String)
     */
    @Override
    public String getProductSupplier(String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=ProductSupplier;ProductSupplier." + cond));
        return csv.getCSV();
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getEntity(java.lang.String, java.lang.String)
     */
    @Override
    public String getEntity(String entity, String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=" + entity + ";" + cond));
        return csv.getCSV();
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getUser(java.lang.String)
     */
    @Override
    public String getUser(String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=LoginUser;LoginUser." + cond));
        return csv.getCSV();
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.connection.IBackendQuery#getCustomer(java.lang.String)
     */
    @Override
    public String getCustomer(String cond) {
        MessageToCSV csv = new MessageToCSV(
                getXMLFromBackend(getURLToBackend() + "?query.select=entity.type=Customer;Customer." + cond));
        return csv.getCSV();
    }
}
