package org.cocome.test;

import org.cocome.tradingsystem.util.Configuration;

import java.util.Properties;

public final class TestConfig {

    private static Properties configData = Configuration
            .loadPropertiesFromClasspath("Configuration.properties");

    public static String getEnterpriseServiceWSDL() {
        return readPropertyValue("org.cocome.cloud.webservice.enterpriseservice.enterpriseServiceWSDL");
    }

    public static String getStoreManagerWSDL() {
        return readPropertyValue("org.cocome.cloud.logic.webservice.storeservice.storeManagerWSDL");
    }

    public static String getPlantManagerWSDL() {
        return readPropertyValue("org.cocome.cloud.logic.webservice.plantservice.plantManagerWSDL");
    }

    private static String readPropertyValue(String attribute) {
        return configData.getProperty(attribute);
    }

    private TestConfig() {
    }
}
