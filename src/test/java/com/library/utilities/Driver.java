package com.library.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class Driver {

    private Driver() {
    }

    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver getDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=1920,1080");
        if (driverPool.get() == null) {

            String browserType = "";


            if (System.getProperty("browser") != null) {
                browserType = System.getProperty("browser");
            } else {
                browserType = ConfigurationReader.getProperty("browser");
            }

            switch (browserType) {
                case "remote-chrome":
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--window-size=1920,1080");

                    options.setCapability("selenoid:options", new HashMap<String, Object>() {{
                        put("sessionTimeout", "10m");
                        put("enableVideo", true);
                        put("browserName", "chrome");
                        put("enableVNC", true);
                    }});

                    try {
                        RemoteWebDriver remoteDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
                        driverPool.set(remoteDriver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "chrome":
                    //WebDriverManager.chromedriver().setup();

                    driverPool.set(new ChromeDriver(chromeOptions));
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
                case "firefox":
                    //WebDriverManager.firefoxdriver().setup();
                    driverPool.set(new FirefoxDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
            }

        }

        return driverPool.get();

    }

    public static <T> T open(String url, Class<T> pageClass) {
        getDriver().get(url);
        try {
            return pageClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {

            driverPool.get().quit();

            driverPool.remove();
        }
    }

}
