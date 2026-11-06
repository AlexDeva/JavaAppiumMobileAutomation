package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ControlsPage;
import pages.HomePage;

public class NativeControlsTest extends BaseTest {
    @Test
    public void canUseDarkThemeControls() {
        ControlsPage controlsPage = new HomePage(getDriver()).openDarkThemeControls();

        controlsPage.selectCheckbox();
        Assert.assertTrue(controlsPage.isCheckboxSelected(), "Checkbox should be selected.");

        controlsPage.selectRadioButton();
        Assert.assertTrue(controlsPage.isRadioButtonSelected(), "Radio button should be selected.");

        String input = "Appium test";
        controlsPage.enterText(input);
        Assert.assertEquals(controlsPage.textFieldValue(), input, "Text field should contain entered text.");
    }
}
