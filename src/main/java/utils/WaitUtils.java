package utils;

import config.FrameworkConfig;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {
    private WaitUtils() {
    }

    public static WebElement waitForVisible(AppiumDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfig.explicitWaitSeconds()))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
