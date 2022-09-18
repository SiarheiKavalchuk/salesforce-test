package com.salesforce.test.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Slf4j
public final class TestDriverManager {

    private static final ThreadLocal<TestDriver> driver = new ThreadLocal<>();


    private TestDriverManager() {
    }

    public static synchronized TestDriver getTestDriver() {
        if (driver.get() == null) {
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

            WebDriverManager.chromedriver().setup();
            var webDriver = new ChromeDriver(getAllowNotificationsChromeOptions());
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(Duration.of(2, ChronoUnit.SECONDS));

            driver.set(new TestDriver(webDriver));
            log.info("Chromedriver created in thread: {}", Thread.currentThread().getName());
        }
        return driver.get();
    }

    public static synchronized void quitTestDriver() {
        if (driver.get() != null) {
            driver.get().quit();
        }
    }

    private static ChromeOptions getAllowNotificationsChromeOptions() {
        var options = new ChromeOptions();
        var prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 1);
        options.setExperimentalOption("prefs", prefs);
        return options;
    }


}
