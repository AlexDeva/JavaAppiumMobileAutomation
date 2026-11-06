package base;

import config.FrameworkConfig;
import io.appium.java_client.AppiumDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

public abstract class BaseTest {
    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    @Parameters({"appType"})
    public void setUp(Method testMethod, @Optional("") String appType) throws Exception {
        String resolvedAppType = appType.isBlank() ? defaultAppType() : appType;
        String platform = FrameworkConfig.platform();
        if (testMethod.getDeclaringClass().getSimpleName().equals("NativeControlsTest")
                && !platform.equalsIgnoreCase("android")) {
            throw new SkipException("The ApiDemos native controls sample is Android-only.");
        }
        DRIVER.set(DriverFactory.createDriver(platform, resolvedAppType));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        AppiumDriver driver = DRIVER.get();
        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            DRIVER.remove();
        }
    }

    protected AppiumDriver getDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Appium driver is not initialized for this test thread.");
        }
        return driver;
    }

    public static AppiumDriver currentDriver() {
        return DRIVER.get();
    }

    protected String defaultAppType() {
        return "native";
    }
}
