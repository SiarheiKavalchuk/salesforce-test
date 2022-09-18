package com.salesforce.test.page;

import com.salesforce.test.page.custom.HasUrl;
import com.salesforce.test.core.driver.TestDriverManager;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage implements HasUrl {

    public BasePage() {
        PageFactory.initElements(TestDriverManager.getTestDriver().getWebDriver(), this);
    }

}
