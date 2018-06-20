package org.cocome.tradingsystem.util;


import java.util.Properties;

/**
 * This config loader is used to load web service properties inside Java SE environments
 * @author Rudolf Biczok
 */
public final class JavaSEConfigLoader {

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

    private JavaSEConfigLoader() {
    }
}
