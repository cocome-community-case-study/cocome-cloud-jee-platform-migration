package org.cocome.cloud.logic.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Utility class for streaming stuff
 *
 * @author Rudolf Biczok
 */
public final class StreamUtil {

    private StreamUtil() {
    }

    /**
     * Creates an non-null stream object
     *
     * @param collection the nullable collection object
     * @param <T>        the type of the collection elements
     * @return an non-null stream object of the given collection
     */
    public static <T> Stream<T> ofNullable(final Collection<T> collection) {
        if (collection == null) {
            return new ArrayList<T>().stream();
        }
        return collection.stream();
    }
}
