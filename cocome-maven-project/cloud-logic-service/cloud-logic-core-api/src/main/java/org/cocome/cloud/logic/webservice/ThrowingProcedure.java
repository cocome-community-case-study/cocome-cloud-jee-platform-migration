package org.cocome.cloud.logic.webservice;

/**
 * Represents a function that performs some work and also can throw an exception
 *
 * @param <E> the type fo exception this function might throw
 */
@FunctionalInterface
public interface ThrowingProcedure<E extends Throwable> {

    /**
     * Executes the function
     *
     * @throws E the exception that might occur during execution
     */
    void run() throws E;
}
