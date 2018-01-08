package org.cocome.cloud.web.data;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

/**
 * Abstract class for for providing CRUD operations
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractDAO<
        TService,
        TTargetContent extends IIdentifiableTO> extends AbstractQuery<TService, TTargetContent> {

    public void create(final ViewData<TTargetContent> viewData)
            throws NotInDatabaseException_Exception, CreateException_Exception {
        final long dbId = createImpl(createServiceClient(), viewData.getData());
        viewData.getData().setId(dbId);
        this.cache.putIfAbsent(viewData.getParentId(), new DBObjectCache<>());
        this.cache.get(viewData.getParentId()).add(viewData);
    }

    public void update(final ViewData<TTargetContent> viewData)
            throws NotInDatabaseException_Exception, UpdateException_Exception {
        updateImpl(createServiceClient(), viewData.getData());
        viewData.setEditingEnabled(false);
    }

    public void delete(final ViewData<TTargetContent> viewData)
            throws NotInDatabaseException_Exception, UpdateException_Exception {
        deleteImpl(createServiceClient(), viewData.getData());
        cache.get(viewData.getParentId()).remove(viewData);
    }

    protected abstract long createImpl(TService service, TTargetContent obj)
            throws CreateException_Exception, NotInDatabaseException_Exception;

    protected abstract void updateImpl(TService service, TTargetContent obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception;

    protected abstract void deleteImpl(TService service, TTargetContent obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception;
}
