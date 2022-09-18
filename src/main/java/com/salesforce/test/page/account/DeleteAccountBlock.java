package com.salesforce.test.page.account;

import com.salesforce.test.page.BaseBlock;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.salesforce.test.core.driver.TestDriverManager.getTestDriver;

public class DeleteAccountBlock extends BaseBlock {

    @FindBy(css = "button[title='Delete']")
    private WebElement deleteButton;

    public void clickDeleteButton() {
        getTestDriver().clickButton(deleteButton);
    }
}
