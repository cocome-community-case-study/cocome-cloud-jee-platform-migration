package org.cocome.cloud.logic.webservice;

import java.util.Objects;

/**
 * Represents a specialized version of {@link java.util.function.Function}
 * In contrast to a {@link java.util.function.Function}, it does also have a
 * throws declaration for one exception time
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type fo exception this function might throw
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @throws E the exception that might occur during execution
     * @return the function result
     */
    R apply(T t) throws E;
}
