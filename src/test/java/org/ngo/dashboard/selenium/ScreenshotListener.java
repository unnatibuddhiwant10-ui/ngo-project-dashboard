package org.ngo.dashboard.selenium;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ScreenshotListener implements TestWatcher {

    private static final String SCREENSHOT_DIR = "screenshots";

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Object testInstance = context.getRequiredTestInstance();
        if (testInstance instanceof BaseSeleniumTest) {
            WebDriver driver = ((BaseSeleniumTest) testInstance).getDriver();
            if (driver instanceof TakesScreenshot) {
                try {
                    Path dir = Paths.get(SCREENSHOT_DIR);
                    if (!Files.exists(dir)) {
                        Files.createDirectories(dir);
                    }
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                    String testName = context.getRequiredTestMethod().getName();
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    Path destination = dir.resolve("FAILED_" + testName + "_" + timestamp + ".png");
                    Files.copy(screenshot.toPath(), destination);
                    System.err.println(">>> [SELENIUM FAILURE HOOK] Failure screenshot saved to: " + destination.toAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Failed to capture screenshot: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println(">>> [SELENIUM TEST PASSED] " + context.getRequiredTestMethod().getName());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {}

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {}
}
