package org.ngo.dashboard.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(ScreenshotListener.class)
public abstract class BaseSeleniumTest {

    @LocalServerPort
    protected int port;

    protected WebDriver driver;

    @BeforeEach
    public void setUpDriver() {
        try {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless=new");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(chromeOptions);
        } catch (Exception e) {
            System.out.println("ChromeDriver initialization failed, attempting fallback to EdgeDriver: " + e.getMessage());
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--headless=new");
            edgeOptions.addArguments("--no-sandbox");
            edgeOptions.addArguments("--disable-dev-shm-usage");
            edgeOptions.addArguments("--disable-gpu");
            edgeOptions.addArguments("--window-size=1920,1080");
            driver = new EdgeDriver(edgeOptions);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
    }

    @AfterEach
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
