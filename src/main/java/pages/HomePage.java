package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

public class HomePage extends BasePage {
    public HomePage(AppiumDriver driver) {
        super(driver);
    }

    public ControlsPage openDarkThemeControls() {
        tap(AppiumBy.accessibilityId("Views"));
        tap(AppiumBy.accessibilityId("Controls"));
        tap(AppiumBy.accessibilityId("2. Dark Theme"));
        return new ControlsPage(driver);
    }
}
