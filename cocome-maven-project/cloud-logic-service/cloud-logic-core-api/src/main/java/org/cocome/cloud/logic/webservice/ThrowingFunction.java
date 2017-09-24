package org.cocome.cloud.logic.webservice;

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
     * @return the function result
     * @throws E the exception that might occur during execution
     */
    R apply(T t) throws E;
}
