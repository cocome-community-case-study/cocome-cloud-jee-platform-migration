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

package org.cocome.cloud.registry.service;

/**
 * Utility class for deriving RMI and JNDI names from component names.
 * 
 * @author Lubomir Bulej
 */
public final class Names {
	public static String getCashDeskName(final int cashDeskIndex) {
		return String.format("CashDesk%d", cashDeskIndex);
	}
	
	private static String getStoreName(final long storeIndex) {
		return String.format("Store%d", storeIndex);
	}
	
	private static String getEnterpriseManagerName(final long enterpriseIndex) {
		return String.format("EnterpriseManager%d", enterpriseIndex);
	}
	
	private static String getEnterpriseReportingName(final long enterpriseIndex) {
		return String.format("EnterpriseReporting%d", enterpriseIndex);
	}
	
	private static String getLoginManagerName(final long enterpriseIndex) {
		return String.format("LoginManager%d", enterpriseIndex);
	}
	
	public static String getBankRemoteName(final String bankName) {
		return String.format("Bank.%s", bankName);
	}

	private static String getEnterpriseRemoteName(final String enterpriseName) {
		return String.format("EnterpriseApplication.%s", enterpriseName);
	}
	
	private static String getStoreRemoteName(final String storeName) {
		return String.format("StoreApplication.%s", storeName);
	}

	private static String getReportingRemoteName(final String reportingName) {
		return String.format("ReportingApplication.%s", reportingName);
	}

	public static String getProductDispatcherRemoteName(final String dispatcherName) {
		return String.format("ProductDispatcher.%s", dispatcherName);
	}
	
	public static String getCashDeskRemoteName(final String storeName, final String cashDeskName) {
		return String.format("%s/%s", storeName, cashDeskName);
	}
	
	public static String getCashDeskComponentRegistryName(final String cashDeskRemoteName, final String componentName) {
		return String.format("%s-%s", cashDeskRemoteName, componentName);
	}
	
	public static String getEnterpriseManagerRegistryName(final long enterpriseIndex) {
		return getEnterpriseRemoteName(getEnterpriseManagerName(enterpriseIndex));
	}
	
	public static String getEnterpriseManagerRegistryName(final String enterpriseName) {
		return getEnterpriseRemoteName(String.format("EnterpriseManager.%s", enterpriseName));
	}
	
	public static String getEnterpriseReportingRegistryName(final long enterpriseIndex) {
		return getReportingRemoteName(getEnterpriseReportingName(enterpriseIndex));
	}
	
	public static String getStoreManagerRegistryName(final long storeIndex) {
		return getStoreRemoteName(getStoreName(storeIndex));
	}
	
	public static String getLoginManagerRegistryName(final long enterpriseIndex) {
		return getEnterpriseRemoteName(getLoginManagerName(enterpriseIndex));
	}
	
	public static String getCashDeskRegistryName(final long storeIndex, final int cashDeskIndex) {
		return getCashDeskRemoteName(getStoreName(storeIndex), getCashDeskName(cashDeskIndex));
	}
}
