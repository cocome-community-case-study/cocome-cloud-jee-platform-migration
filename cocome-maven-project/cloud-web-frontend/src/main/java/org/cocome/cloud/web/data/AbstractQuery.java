package org.cocome.cloud.web.data;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
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
 * Abstract class for for providing query operations
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractQuery<
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
        return this.getAllByParentId(parent.getData().getId());
    }

    public Collection<TTargetContent> getAllByParent(@NotNull IIdentifiableTO parent)
            throws NotInDatabaseException_Exception {
        return this.getAllByParentId(parent.getId())
                .stream().map(ViewData::getData).collect(Collectors.toList());
    }

    private Collection<ViewData<TTargetContent>> getAllByParentId(@NotNull long parentId)
            throws NotInDatabaseException_Exception {
        if (!queried.containsKey(parentId)) {
            queried.put(parentId, true);
            cache.put(parentId, new DBObjectCache<>());
            cache.get(parentId).addAll(this.queryAllByParentObj(parentId));
        }
        return this.cache.get(parentId).getAll();
    }

    private Collection<ViewData<TTargetContent>> queryAllByParentObj(
            @NotNull long parentId)
            throws NotInDatabaseException_Exception {
        LOG.debug("Querying production unit classes");

        final TService plantManager = createServiceClient();
        return StreamUtil.ofNullable(
                queryAllByParentObj(plantManager, parentId))
                .map(this::createViewDataInstance).collect(Collectors.toList());
    }

    protected abstract TService createServiceClient() throws NotInDatabaseException_Exception;

    protected abstract Collection<TTargetContent> queryAllByParentObj(TService service, final long id)
            throws NotInDatabaseException_Exception;

    public abstract ViewData<TTargetContent> createViewDataInstance(final TTargetContent target);

    protected abstract TTargetContent queryById(TService serviceClient, long dbId)
            throws NotInDatabaseException_Exception;

}
