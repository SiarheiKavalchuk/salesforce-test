package com.salesforce.test.page.login;

import com.salesforce.test.page.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.salesforce.test.core.driver.TestDriverManager.getTestDriver;

public class RegisterPhonePage extends BasePage {

    @FindBy(xpath = "//a[contains(@href,'_ui/identity/phone/AddPhoneNumber?retURL')]")
    private WebElement dontRegisterPhoneButton;


    public void clickDontRegisterPhoneButton() {
        getTestDriver().clickButton(dontRegisterPhoneButton);
    }

    @Override
    public String getUrl() {
        return "_ui/identity/phone/AddPhoneNumber?retURL";
    }
}
