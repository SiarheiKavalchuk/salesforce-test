package com.salesforce.test.core.driver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TestDriver {

    private final WebDriver webDriver;

    public TestDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public File takeScreenshot() {
       return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.FILE);
    }

    public void quit() {
        getWebDriver().quit();
    }

    public void openPage(String url) {
        getWebDriver().get(url);
    }

    public String getUrl() {
        return getWebDriver().getCurrentUrl();
    }

    public void enterToField(WebElement field, String value) {
        waitElementToBe(ExpectedConditions.visibilityOf(field));
        field.clear();
        field.sendKeys(value);
    }

    public void clickButton(WebElement button) {
        waitElementToBe(ExpectedConditions.elementToBeClickable(button));
        button.click();
    }

    public String getText(WebElement field) {
        return field.getText();
    }

    public WebElement getElementWithJs(String jsPath) {
        return (WebElement) ((JavascriptExecutor) getWebDriver()).executeScript(jsPath);
    }

    private void waitElementToBe(ExpectedCondition<WebElement> field) {
        new WebDriverWait(getWebDriver(), Duration.of(2, ChronoUnit.SECONDS)).until(field);
    }

}
