package tests;

import base.BaseTest;
import config.FrameworkConfig;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WaitUtils;

public class MobileWebLoginTest extends BaseTest {
    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = AppiumBy.cssSelector("button[type='submit']");
    private static final By PAGE_HEADING = AppiumBy.cssSelector("h2");

    @Override
    protected String defaultAppType() {
        return "web";
    }

    @Test
    public void canLogInToMobileWebDemo() {
        getDriver().get(FrameworkConfig.get("web.base.url"));
        WaitUtils.waitForVisible(getDriver(), USERNAME).sendKeys("tomsmith");
        WaitUtils.waitForVisible(getDriver(), PASSWORD).sendKeys("SuperSecretPassword!");
        WaitUtils.waitForVisible(getDriver(), LOGIN_BUTTON).click();

        Assert.assertEquals(WaitUtils.waitForVisible(getDriver(), PAGE_HEADING).getText(),
                "Secure Area", "Successful login should show the secure area.");
    }
}
