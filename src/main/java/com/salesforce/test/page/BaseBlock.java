package com.salesforce.test.page;

import com.salesforce.test.core.driver.TestDriverManager;
import org.openqa.selenium.support.PageFactory;

public abstract class BaseBlock {

    public BaseBlock() {
        PageFactory.initElements(TestDriverManager.getTestDriver().getWebDriver(), this);
    }

}
