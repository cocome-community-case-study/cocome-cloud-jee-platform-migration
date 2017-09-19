package org.cocome.tradingsystem.remote.access.connection;

import org.apache.log4j.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Implements functionality to manipulate data in the backend using
 * the CSV-based serviceadapter interface.
 *
 * @author Tobias Pöppke
 */
@Dependent
public class CSVBackendConnection implements IPersistenceConnection {

    @Inject
    private String backendHost;

    @Inject
    private String backendPort;

    @Inject
    private String backendSetDataURL;

    private static final Logger LOG = Logger.getLogger(CSVBackendConnection.class);

    /*
     * the response of the post as message
     */
    private String message;

    private String getSetDataBackendURL() {
        return "http://" + backendHost + ":" + backendPort + backendSetDataURL;
    }

    private HttpURLConnection getURLConnection(String type, String entity) throws IOException {
        URL url = new URL(getSetDataBackendURL() +
                "?query." + type + "=" + entity);
        return (HttpURLConnection) url.openConnection();
    }

    private void setConnectionAttributes(HttpURLConnection con, String requestMethod) throws ProtocolException {
        con.setRequestMethod(requestMethod);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", "application/csv");
    }

    private String writeDataToBackend(HttpURLConnection con, String content) throws IOException {
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        LOG.debug("Sending content [" + content + "] to " + con.getURL().toString());

        wr.writeBytes(content);

        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append(System.getProperty("line.separator"));
        }
        in.close();

        LOG.debug("Response from backend:\n" + response.toString());

        return response.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void sendUpdateQuery(String entity, String header, String content) throws IOException {
        HttpURLConnection con = getURLConnection("update", entity);
        setConnectionAttributes(con, HttpMethod.PUT);
        this.message = writeDataToBackend(con, header + "\n" + content);
    }

    @Override
    public void sendDeleteQuery(String entity, String header, String content) throws IOException {
        HttpURLConnection con = getURLConnection("delete", entity);
        //HttpMethod.DELETE is not possible here, because service-adapter is not really restful
        setConnectionAttributes(con, HttpMethod.PUT);
        this.message = writeDataToBackend(con, header + "\n" + content);
    }

    /**
     * {@inheritDoc}
     */
    public void sendCreateQuery(String entity, String header, String content) throws IOException {
        HttpURLConnection con = getURLConnection("insert", entity);
        setConnectionAttributes(con, HttpMethod.POST);
        this.message = writeDataToBackend(con, header + "\n" + content);
    }

    /**
     * {@inheritDoc}
     */
    public String getResponse() {
        return this.message;
    }
}
