package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public final class HybridContextUtils {
    private HybridContextUtils() {
    }

    public static Set<String> availableContexts(AppiumDriver driver) {
        return contextDriver(driver).getContextHandles();
    }

    public static String switchToWebView(AppiumDriver driver, Duration timeout) {
        SupportsContextSwitching contextDriver = contextDriver(driver);
        String context = new WebDriverWait(driver, timeout).until(currentDriver ->
                contextDriver.getContextHandles().stream()
                        .filter(handle -> handle.startsWith("WEBVIEW"))
                        .findFirst()
                        .orElse(null));
        if (context == null) {
            throw new TimeoutException("No WEBVIEW context became available within " + timeout + ".");
        }
        contextDriver.context(context);
        return context;
    }

    public static void switchToNative(AppiumDriver driver) {
        contextDriver(driver).context("NATIVE_APP");
    }

    private static SupportsContextSwitching contextDriver(AppiumDriver driver) {
        if (!(driver instanceof SupportsContextSwitching contextDriver)) {
            throw new IllegalArgumentException("The active Appium driver does not support context switching.");
        }
        return contextDriver;
    }
}
