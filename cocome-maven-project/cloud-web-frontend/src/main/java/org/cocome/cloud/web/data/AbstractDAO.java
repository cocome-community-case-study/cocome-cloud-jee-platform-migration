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
        TTargetContent extends IIdentifiableTO> implements Serializable {

    private static final Logger LOG = Logger.getLogger(ProductionUnitOperationDAO.class);

    private final Map<Long, Boolean> queried = new HashMap<>();
    protected final Map<Long, DBObjectCache<ViewData<TTargetContent>>> cache = new HashMap<>();

    public TTargetContent find(final long dbId) throws NotInDatabaseException_Exception {
        for (final DBObjectCache<ViewData<TTargetContent>> entry : cache.values()) {
            if (entry.get(dbId) != null) {
                return entry.get(dbId).getData();
            }
        }
        final TTargetContent instance = this.queryById(createServiceClient(), dbId);
        final ViewData<TTargetContent> viewData = createViewDataInstance(instance);
        this.cache.putIfAbsent(viewData.getParentId(), new DBObjectCache<>());
        this.cache.get(viewData.getParentId()).add(viewData);
        return instance;
    }

    public Collection<ViewData<TTargetContent>> getAllByParentObj(@NotNull ViewData<?> parent)
            throws NotInDatabaseException_Exception {
        if (!queried.containsKey(parent.getData().getId())) {
            queried.put(parent.getData().getId(), true);
            cache.put(parent.getData().getId(), new DBObjectCache<>());
            cache.get(parent.getData().getId()).addAll(this.queryAllByParentObj(parent));
        }
        return this.cache.get(parent.getData().getId()).getAll();
    }

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

    private Collection<ViewData<TTargetContent>> queryAllByParentObj(
            @NotNull ViewData<?> parantViewData)
            throws NotInDatabaseException_Exception {
        LOG.debug("Querying production unit classes");

        final TService plantManager = createServiceClient();
        return StreamUtil.ofNullable(
                queryAllByParentObj(plantManager, parantViewData.getData().getId()))
                .map(this::createViewDataInstance).collect(Collectors.toList());
    }

    protected abstract TService createServiceClient() throws NotInDatabaseException_Exception;

    protected abstract Collection<TTargetContent> queryAllByParentObj(TService service, final long id)
            throws NotInDatabaseException_Exception;

    public abstract ViewData<TTargetContent> createViewDataInstance(final TTargetContent target);

    protected abstract long createImpl(TService service, TTargetContent obj)
            throws CreateException_Exception;

    protected abstract void updateImpl(TService service, TTargetContent obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception;

    protected abstract void deleteImpl(TService service, TTargetContent obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception;

    protected abstract TTargetContent queryById(TService serviceClient, long dbId)
            throws NotInDatabaseException_Exception;

}
