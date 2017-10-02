package org.cocome.cloud.logic.webservice;

/**
 * Represents a specialized version of {@link java.util.function.Supplier}
 * In contrast to a {@link java.util.function.Supplier}, it does also have a
 * throws declaration for one exception type
 *
 * @param <T> the type of the output
 * @param <E> the type fo exception this function might throw
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws E;
}
