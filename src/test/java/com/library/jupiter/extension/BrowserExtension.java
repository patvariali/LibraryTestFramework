package com.library.jupiter.extension;

import com.library.utilities.Driver;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

public class BrowserExtension implements TestExecutionExceptionHandler,
        AfterEachCallback,
        LifecycleMethodExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        capturePageSourceWithStyles(Driver.getDriver());
        browserConsoleLogs();
        addVideo();
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        capturePageSourceWithStyles(Driver.getDriver());
        browserConsoleLogs();
        addVideo();
        throw throwable;
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        capturePageSourceWithStyles(Driver.getDriver());
        browserConsoleLogs();
        addVideo();
        throw throwable;
    }

    private void doScreenShot() {
        if (Driver.getDriver() != null) {
            Allure.addAttachment(
                    "Screenshot of the test end",
                    new ByteArrayInputStream(
                            Objects.requireNonNull(
                                    ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES)
                            )
                    )
            );
        }
    }


    @Attachment(value = "Page source", type = "text/html")
    public String capturePageSourceWithStyles(WebDriver driver) {
        if (driver instanceof JavascriptExecutor) {
            try {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                String cssStyles = (String) jsExecutor.executeScript(
                        "return Array.from(document.styleSheets).flatMap(sheet => " +
                                "Array.from(sheet.cssRules || []).map(rule => rule.cssText)).join('\\n')");
                String pageSource = driver.getPageSource();
                String styleTag = "<style>" + cssStyles + "</style>";
                // Вставляю стили в конце head
                pageSource = pageSource.replace("</head>", styleTag + "</head>");
                return pageSource;
            } catch (JavascriptException e) {
                System.out.println("Failed to execute JavaScript for fetching CSS styles. Returning page source without styles.");
                return driver.getPageSource();
            }
        } else {
            System.out.println("Driver does not support JavaScript execution.");
            return driver.getPageSource();
        }
    }


    @Attachment(value = "{attachName}", type = "text/plain")
    private String attachAsText(String attachName, String message) {
        return message;
    }

    private void browserConsoleLogs() {
        if (Driver.getDriver() != null) {
            LogEntries logEntries = Driver.getDriver().manage().logs().get(LogType.BROWSER);

            StringBuilder allLogs = new StringBuilder();
            for (LogEntry logEntry : logEntries) {
                allLogs.append(new Date(logEntry.getTimestamp()))
                        .append(" ")
                        .append(logEntry.getLevel())
                        .append(" ")
                        .append(logEntry.getMessage())
                        .append("\n");
            }

            attachAsText("Browser console logs", String.valueOf(allLogs));
        }
    }


    @Attachment(value = "Video", type = "text/html", fileExtension = ".html")
    private String addVideo() {

        return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                + getVideoUrl()
                + "' type='video/mp4'></video></body></html>";

    }

    private URL getVideoUrl() {

        String videoUrl = "http://localhost:8080/video/" + ((RemoteWebDriver) Driver.getDriver()).getSessionId().toString() + ".mp4";

        try {
            return new URL(videoUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (Driver.getDriver() != null) {
            Driver.closeDriver();
        }
    }
}

