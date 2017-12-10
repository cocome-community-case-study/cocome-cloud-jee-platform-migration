package org.cocome.cloud.web.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Object cache used to cache a list of objects. It aims to provide methods for
 * accessing and deleting objects in the cache in linear time
 *
 * @param <T> the type of database objects to cache
 * @author Rudolf Biczok
 */
public class DBObjectCache<T extends ViewData> {
    private final Map<Long, T> objects = new LinkedHashMap<>();

    public boolean isEmpty() {
        return this.objects.isEmpty();
    }

    public void add(final T object) {
        this.objects.put(object.getData().getId(), object);
    }

    public void addAll(final Collection<T> objects) {
        for (final T object : objects) {
            this.add(object);
        }
    }

    public Collection<T> getAll() {
        return this.objects.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public T get(final long dbId) {
        return this.objects.get(dbId);
    }

    public T remove(final long dbId) {
        return this.objects.remove(dbId);
    }

    public void remove(final T obj) {
        this.objects.remove(obj.getData().getId());
    }
}
