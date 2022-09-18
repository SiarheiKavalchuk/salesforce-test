package com.salesforce.test.page.account;

import com.salesforce.test.page.BaseBlock;
import com.salesforce.test.page.custom.PageOpener;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.salesforce.test.core.driver.TestDriverManager.getTestDriver;

@Getter
public abstract class BaseAccountCreateEditBlock extends BaseBlock implements PageOpener {

    @FindBy(xpath = "//label/span[contains(text(),'Account Name')]/following::input[1]")
    private WebElement accountNameField;
    @FindBy(xpath = "//label/span[contains(text(),'Phone')]/following::input[1]")
    private WebElement phoneField;
    @FindBy(xpath = "//label/span[contains(text(),'Website')]/following::input[1]")
    private WebElement websiteField;
    @FindBy(css = "button[title='Save']")
    private WebElement saveButton;

    @SuppressWarnings("unchecked")
    public <T extends BaseAccountCreateEditBlock> T enterAccountName(String value) {
        getTestDriver().enterToField(accountNameField, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseAccountCreateEditBlock> T enterPhone(String value) {
        getTestDriver().enterToField(phoneField, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseAccountCreateEditBlock> T enterWebsite(String value) {
        getTestDriver().enterToField(websiteField, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseAccountCreateEditBlock> T clickSaveButton() {
        getTestDriver().clickButton(saveButton);
        return (T) this;
    }

    @Override
    public ExpectedCondition<WebElement> waitForCondition() {
        return ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//h2[contains(@class,'inlineTitle')]/ancestor::div[contains(@class,'modal-body')]"));
    }

}
