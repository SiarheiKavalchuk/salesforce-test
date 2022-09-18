package com.salesforce.test.page.custom;

import com.salesforce.test.util.PropertiesProvider;

import static com.salesforce.test.core.constant.AppConstants.APP_PROPERTY_APP_URL;

public interface HasUrl {
    String BASE_URL = PropertiesProvider.getAppPropertyValue(APP_PROPERTY_APP_URL);

    String getUrl();
}
