package org.cocome.cloud.web.data;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.cloud.web.data.plantdata.ProductionUnitOperationDAO;
import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Abstract
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractDAO<
        TService,
        TTargetContent extends IIdentifiableTO,
        TParentContent extends IIdentifiableTO> implements Serializable {

    private static final Logger LOG = Logger.getLogger(ProductionUnitOperationDAO.class);

    protected final Map<Long, Boolean> queried = new HashMap<>();
    protected final Map<Long, DBObjectCache<ViewData<TTargetContent>>> cache = new HashMap<>();

    public Collection<ViewData<TTargetContent>> getAllByParentObj(@NotNull ViewData<TParentContent> parent)
            throws NotInDatabaseException_Exception {
        if (!queried.containsKey(parent.getData().getId())) {
            queried.put(parent.getData().getId(), true);
            cache.put(parent.getData().getId(), new DBObjectCache<>());
            cache.get(parent.getData().getId()).addAll(this.queryAllByParentObj(parent));
        }
        return this.cache.get(parent.getData().getId()).getAll();
    }

    public void create(final ViewData<TTargetContent> puOperation) throws NotInDatabaseException_Exception, CreateException_Exception {
        final long dbId = createImpl(createServiceClient(puOperation.getServiceId()), puOperation.getData());
        puOperation.getData().setId(dbId);
        this.cache.putIfAbsent(puOperation.getParentId(), new DBObjectCache<>());
        this.cache.get(puOperation.getParentId()).add(puOperation);
    }

    public void update(final ViewData<TTargetContent> puOperation) throws NotInDatabaseException_Exception, UpdateException_Exception {
        updateImpl(createServiceClient(puOperation.getServiceId()), puOperation.getData());
        puOperation.setEditingEnabled(false);
    }

    public void delete(final ViewData<TTargetContent> puOperation) throws NotInDatabaseException_Exception, UpdateException_Exception {
        deleteImpl(createServiceClient(puOperation.getServiceId()), puOperation.getData());
        cache.get(puOperation.getParentId()).remove(puOperation);
    }

    private Collection<ViewData<TTargetContent>> queryAllByParentObj(@NotNull ViewData<TParentContent> puc)
            throws NotInDatabaseException_Exception {
        LOG.debug("Querying production unit classes");

        final TService plantManager = createServiceClient(puc.getServiceId());
        return StreamUtil.ofNullable(
                queryAllByParentObj(plantManager, puc.getData().getId()))
                .map(this::createViewDataInstance).collect(Collectors.toList());
    }

    public abstract TService createServiceClient(final long target) throws NotInDatabaseException_Exception;

    public abstract Collection<TTargetContent> queryAllByParentObj(TService service, final long id) throws NotInDatabaseException_Exception;

    public abstract ViewData<TTargetContent> createViewDataInstance(final TTargetContent target);

    protected abstract long createImpl(TService service, TTargetContent viewData) throws CreateException_Exception;

    protected abstract void updateImpl(TService service, TTargetContent viewData) throws UpdateException_Exception, NotInDatabaseException_Exception;

    protected abstract void deleteImpl(TService service, TTargetContent viewData) throws UpdateException_Exception, NotInDatabaseException_Exception;

}
