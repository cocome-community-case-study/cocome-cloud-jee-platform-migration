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

import javax.ejb.Local;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

/**
 * @author Yannick Welsch
 */
@Local
public interface ITransactionContextLocal {

	/**
	 * Starts a new transaction.
	 * @throws SystemException 
	 * @throws NotSupportedException 
	 * @throws Exception 
	 */
	void beginTransaction() throws Exception;

	/**
	 * Commits the current transaction.
	 * @throws SystemException 
	 * @throws HeuristicRollbackException 
	 * @throws HeuristicMixedException 
	 * @throws RollbackException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 */
	void commit() throws Exception;

	/**
	 * Rolls back the current transaction.
	 */
	void rollback() throws Exception;

	/**
	 * Checks whether this transaction is still active.
	 * 
	 * @return
	 *         {@code true} if this transaction is active, {@code false} otherwise
	 */
	boolean isActive() throws Exception;

}
