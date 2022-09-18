package com.salesforce.test.core.driver;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public interface Wait {

    static void forCondition(ExpectedCondition<?> condition, int seconds) {
        new WebDriverWait(TestDriverManager.getTestDriver().getWebDriver(), Duration.of(seconds, ChronoUnit.SECONDS))
                .until(condition);
    }

}
