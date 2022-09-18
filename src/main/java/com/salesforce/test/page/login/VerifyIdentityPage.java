package com.salesforce.test.page.login;

import com.salesforce.test.page.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.salesforce.test.core.driver.TestDriverManager.getTestDriver;

public class VerifyIdentityPage extends BasePage {

    @FindBy(id = "emc")
    private WebElement verificationCodeField;
    @FindBy(id = "save")
    private WebElement verifyButton;

    public VerifyIdentityPage enterCode(String code) {
        getTestDriver().enterToField(verificationCodeField, code);
        return this;
    }

    public VerifyIdentityPage clickVerifyButton() {
        getTestDriver().clickButton(verifyButton);
        return this;
    }

    @Override
    public String getUrl() {
        return "_ui/identity/verification";
    }
}
