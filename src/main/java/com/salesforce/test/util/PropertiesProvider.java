package com.salesforce.test.util;

import com.salesforce.test.core.exceptions.AutomationBugException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.salesforce.test.core.constant.AppConstants.APP_PROPERTIES_PATH;

public final class PropertiesProvider {

    private static Properties appProperties;

    private PropertiesProvider() {
    }

    public static String getAppPropertyValue(String key) {
        if (appProperties == null) {
            appProperties = getProperties(APP_PROPERTIES_PATH);
        }
        var value = appProperties.getProperty(key);
        if (value == null) {
            throw new AutomationBugException("Property not found in application properties by key: " + key);
        }
        return value;
    }

    public static Properties getProperties(String pathInResourcesFolder) {
        try (InputStream input = PropertiesProvider.class.getClassLoader().getResourceAsStream(pathInResourcesFolder)) {
            if (input == null) {
                throw new AutomationBugException("Properties not found by path in resources folder: " + pathInResourcesFolder);
            }
            var properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new AutomationBugException("Unable to read properties: " + e.getMessage());
        }
    }
}

