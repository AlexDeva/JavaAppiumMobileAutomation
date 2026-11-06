package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class ControlsPage extends BasePage {
    private static final By CHECKBOX = AppiumBy.id("io.appium.android.apis:id/check1");
    private static final By RADIO_BUTTON = AppiumBy.id("io.appium.android.apis:id/radio1");
    private static final By TEXT_FIELD = AppiumBy.id("io.appium.android.apis:id/edit");

    public ControlsPage(AppiumDriver driver) {
        super(driver);
    }

    public void selectCheckbox() {
        if (!isSelected(CHECKBOX)) {
            tap(CHECKBOX);
        }
    }

    public boolean isCheckboxSelected() {
        return isSelected(CHECKBOX);
    }

    public void selectRadioButton() {
        tap(RADIO_BUTTON);
    }

    public boolean isRadioButtonSelected() {
        return isSelected(RADIO_BUTTON);
    }

    public void enterText(String text) {
        enterText(TEXT_FIELD, text);
    }

    public String textFieldValue() {
        return waitForVisible(TEXT_FIELD).getText();
    }
}
