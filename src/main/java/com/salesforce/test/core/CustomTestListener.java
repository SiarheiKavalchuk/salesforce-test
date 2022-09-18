package com.salesforce.test.core;

import com.salesforce.test.util.SystemNameProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.IConfigurationListener;
import org.testng.ITestResult;
import org.testng.internal.IResultListener;

import java.io.File;
import java.io.IOException;

import static com.salesforce.test.core.driver.TestDriverManager.getTestDriver;

@Slf4j
public class CustomTestListener implements IResultListener, IConfigurationListener {

    @Override
    public void onConfigurationFailure(ITestResult tr) {
        takeScreenshot(tr.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        takeScreenshot(result.getName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        takeScreenshot(result.getName());
    }

    private void takeScreenshot(String name) {
        if (getTestDriver() != null) {
            log.info("Attempt to take screenshot");
            var screenshot = getTestDriver().takeScreenshot();

            var path = generatePath(name);
            log.info("Path to screenshot: " + path);

            try {
                FileUtils.copyFile(screenshot, new File(path));
            } catch (IOException e) {
                log.warn("Failed to create screenshot: " + e.getMessage());
            }
        }
    }

    private String generatePath(String name) {
        var pathToFolder = SystemNameProvider.isWindows() ? "\\screenshots\\" : "/screenshots/";
        return new File(StringUtils.EMPTY).getAbsolutePath() + pathToFolder + System.currentTimeMillis() + "-" + name + ".png";
    }

}
