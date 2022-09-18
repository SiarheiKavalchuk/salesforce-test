package com.salesforce.test.page.login;

import com.salesforce.test.page.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.salesforce.test.core.driver.TestDriverManager.getTestDriver;

public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private WebElement usernameField;
    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(id = "Login")
    private WebElement loginButton;

    public LoginPage openLoginPage() {
        getTestDriver().openPage(getUrl());
        return new LoginPage();
    }

    public LoginPage enterUsername(String username) {
        getTestDriver().enterToField(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        getTestDriver().enterToField(passwordField, password);
        return this;
    }

    public LoginPage clickLoginButton() {
        getTestDriver().clickButton(loginButton);
        return this;
    }

    @Override
    public String getUrl() {
        return BASE_URL;
    }
}
