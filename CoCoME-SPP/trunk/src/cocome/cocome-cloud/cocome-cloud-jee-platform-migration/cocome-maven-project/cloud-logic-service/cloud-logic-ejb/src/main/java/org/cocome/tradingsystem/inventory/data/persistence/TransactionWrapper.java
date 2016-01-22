/***************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.data.persistence;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.cocome.tradingsystem.util.Cloud;

/**
 * Utility class for executing operations within transaction context. The class
 * provides several {@code execute()} methods for executing different kinds of
 * operations through a callback interface.
 * 
 * @author Lubomir Bulej
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TransactionWrapper {
	
	@Inject @Cloud
	private static IPersistenceLocal __persistence__;

	private static final class DummyException extends Exception {
		// intentionally empty, serves as a dummy checked exception type
	};

	//

	public TransactionWrapper() {
		// utility class, not to be instantiated
	}

	//

	/**
	 * Simple operation that does not return any value and does not throw any
	 * exceptions.
	 */
	public interface SimpleOperation {
		void execute(IPersistenceContextLocal pctx);
	}

	public static void execute(final SimpleOperation operation) {
		try {
			// ignore dummy return value
			executeInTransaction(new InternalOperation<Object, DummyException, DummyException>() {
				@Override
				public Object execute(final IPersistenceContextLocal pctx) {
					operation.execute(pctx);
					return null;
				}
			});

		} catch (final DummyException impossible) {
			// no one can throw this exception, just silence the compiler
			throw new AssertionError("unexpected exception: " + impossible);
		}
	}

	//

	/**
	 * Simple operation that does not return any value and does not throw any
	 * exceptions.
	 */
	public interface CheckedSimpleOperation<E extends Exception> {
		void execute(IPersistenceContextLocal pctx) throws E;
	}

	public static <E extends Exception> void execute(
			final CheckedSimpleOperation<E> operation
			) throws E {
		try {
			// ignore dummy return value
			executeInTransaction(new InternalOperation<Object, E, DummyException>() {
				@Override
				public Object execute(final IPersistenceContextLocal pctx) throws E {
					operation.execute(pctx);
					return null;
				}
			});

		} catch (final DummyException impossible) {
			// no one can throw this exception, just silence the compiler
			throw new AssertionError("unexpected exception: " + impossible);
		}
	}

	//

	/**
	 * Normal operation that returns a result but does not throw any exceptions.
	 */
	public interface Operation<R> {
		R execute(IPersistenceContextLocal pctx);
	}

	public static <R> R execute(final Operation<R> operation) {
		try {
			return executeInTransaction(new InternalOperation<R, DummyException, DummyException>() {
				@Override
				public R execute(final IPersistenceContextLocal pctx) {
					return operation.execute(pctx);
				}
			});

		} catch (final DummyException impossible) {
			// no one can throw this exception, just silence the compiler
			throw new AssertionError("unexpected exception: " + impossible);
		}
	}

	//

	/**
	 * Less common, but still normal operation that returns a result and throws
	 * a single checked exception.
	 */
	public interface CheckedOperation<R, E extends Exception> {
		R execute(IPersistenceContextLocal pctx) throws E;
	}

	public static <R, E extends Exception> R execute(
			final CheckedOperation<R, E> operation
			) throws E {
		try {
			return executeInTransaction(new InternalOperation<R, E, DummyException>() {
				@Override
				public R execute(final IPersistenceContextLocal pctx) throws E {
					return operation.execute(pctx);
				}
			});

		} catch (final DummyException impossible) {
			// no one can throw this exception, just silence the compiler
			throw new AssertionError("unexpected exception: " + impossible);
		}
	}

	//

	public static void persistObject(final Object object) {
		TransactionWrapper.execute(new SimpleOperation() {
			@Override
			public void execute(final IPersistenceContextLocal pctx) {
				pctx.makePersistent(object);
			}
		});
	}

	//

	/**
	 * Internal interface for operations that return a result an can throw two
	 * checked exceptions. This operation is used internally to implement the
	 * execution of the given operation in a transaction context.
	 */
	private interface InternalOperation<R, E1 extends Exception, E2 extends Exception> {
		R execute(IPersistenceContextLocal pctx) throws E1, E2;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private static <R, E1 extends Exception, E2 extends Exception> R executeInTransaction(
			final InternalOperation<R, E1, E2> operation
			) throws E1, E2 {
		return executeInContext(new InternalOperation<R, E1, E2>() {
			@Override
			public R execute(final IPersistenceContextLocal pctx) throws E1, E2 {
				final R result = operation.execute(pctx);
				return result;
			}
		});
	}

	private static <R, E1 extends Exception, E2 extends Exception> R executeInContext(
			final InternalOperation<R, E1, E2> operation
			) throws E1, E2 {
		final IPersistenceContextLocal pctx = __persistence__.getPersistenceContext();

		try {
			return operation.execute(pctx);

		} finally {
			pctx.close();
		}
	}

}
