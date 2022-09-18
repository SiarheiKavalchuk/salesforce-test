package com.salesforce.test.page.custom;

import com.salesforce.test.core.driver.Wait;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static com.salesforce.test.core.driver.TestDriverManager.getTestDriver;

public interface PageOpener extends HasUrl {

    ExpectedCondition<?> waitForCondition();

    default void openPage() {
        getTestDriver().openPage(getUrl());
        Wait.forCondition(waitForCondition(), 10);
    }

}
