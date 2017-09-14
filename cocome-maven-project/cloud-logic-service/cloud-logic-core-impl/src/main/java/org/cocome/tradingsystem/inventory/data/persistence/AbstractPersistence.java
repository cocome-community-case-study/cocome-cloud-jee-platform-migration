package org.cocome.tradingsystem.inventory.data.persistence;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.remote.access.connection.IPersistenceConnection;

import javax.ejb.CreateException;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Abstract class for implementing persistence classes
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractPersistence<T> implements IPersistence<T> {
    private static final Logger LOG = Logger.getLogger(AbstractPersistence.class);

    public static final String SEPARATOR = ";";

    @Inject
    IPersistenceConnection postData;

    protected String encodeString(String string) {
        // If the string is encoded there are problems with the
        // queries because of the way the service adapter handles
        // them. Leaving this here for future consideration.
        return string;
        // return QueryParameterEncoder.encodeQueryString(string);
    }

    @Override
    public void createEntity(T entity) throws CreateException {
        String content = convertToSubmittableContent(entity);
        try {
            postData.sendCreateQuery(getEntityName(), getContentHeader(), content);
        } catch (IOException e) {
            // TODO perhaps throw this exception to caller?
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (postData.getResponse().contains("FAIL") || !postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }


    @Override
    public void updateEntity(T entity) throws UpdateException {
        String content = convertToSubmittableContentWithId(entity);
        try {
            postData.sendUpdateQuery(getEntityName(), getContentHeaderWithId(), content);
        } catch (IOException e) {
            // TODO perhaps throw this exception to caller?
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not update entity!", e);
        }

        if (postData.getResponse().contains("FAIL") || !postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not update entity!");
        }
    }

    @Override
    public void deleteEntity(T entity) throws UpdateException {
        String content = convertToSubmittableContentWithId(entity);
        try {
            postData.sendDeleteQuery(getEntityName(), getContentHeaderWithId(), content);
        } catch (IOException e) {
            // TODO perhaps throw this exception to caller?
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not delete entity!", e);
        }

        if (postData.getResponse().contains("FAIL") || !postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not delete entity!");
        }
    }

    protected abstract String convertToSubmittableContent(T entity);

    protected abstract String convertToSubmittableContentWithId(T entity);

    protected abstract String getContentHeader();

    protected abstract String getContentHeaderWithId();

    protected abstract String getEntityName();
}
