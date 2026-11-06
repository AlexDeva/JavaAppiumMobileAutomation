package base;

import config.FrameworkConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Locale;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static AppiumDriver createDriver(String platform, String appType) throws Exception {
        String normalizedPlatform = platform.toLowerCase(Locale.ROOT);
        if (!appType.equalsIgnoreCase("native") && !appType.equalsIgnoreCase("web")
                && !appType.equalsIgnoreCase("calculator")) {
            throw new IllegalArgumentException(
                    "Unsupported app type '" + appType + "'. Expected native, web, or calculator.");
        }
        if (appType.equalsIgnoreCase("calculator") && !normalizedPlatform.equals("android")) {
            throw new IllegalArgumentException("The Samsung Calculator sample is Android-only.");
        }
        URL serverUrl = URI.create(FrameworkConfig.appiumServerUrl()).toURL();
        String appPath = FrameworkConfig.appPath();

        return switch (normalizedPlatform) {
            case "android" -> new AndroidDriver(serverUrl, androidOptions(appType, appPath));
            case "ios" -> new IOSDriver(serverUrl, iosOptions(appType, appPath));
            default -> throw new IllegalArgumentException(
                    "Unsupported platform '" + platform + "'. Expected android or ios.");
        };
    }

    private static UiAutomator2Options androidOptions(String appType, String appPath) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setDeviceName(FrameworkConfig.deviceName())
                .setNewCommandTimeout(Duration.ofSeconds(120));

        if (appType.equalsIgnoreCase("web")) {
            options.setCapability("browserName", FrameworkConfig.browserName());
        } else if (appType.equalsIgnoreCase("calculator")) {
            options.setAppPackage(FrameworkConfig.calculatorAppPackage())
                    .setAppActivity(FrameworkConfig.calculatorAppActivity());
        } else if (!appPath.isBlank()) {
            options.setApp(resolveAppPath(appPath));
        } else {
            options.setAppPackage(FrameworkConfig.androidAppPackage())
                    .setAppActivity(FrameworkConfig.androidAppActivity());
        }
        return options;
    }

    private static XCUITestOptions iosOptions(String appType, String appPath) {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setDeviceName(FrameworkConfig.deviceName())
                .setNewCommandTimeout(Duration.ofSeconds(120));

        if (appType.equalsIgnoreCase("web")) {
            options.setCapability("browserName", FrameworkConfig.browserName());
        } else if (!appPath.isBlank()) {
            options.setApp(resolveAppPath(appPath));
        } else if (!FrameworkConfig.iosBundleId().isBlank()) {
            options.setBundleId(FrameworkConfig.iosBundleId());
        } else {
            throw new IllegalArgumentException(
                    "Set app.path or ios.bundle.id to launch a native iOS application.");
        }
        return options;
    }

    private static String resolveAppPath(String appPath) {
        Path path = Path.of(appPath).toAbsolutePath().normalize();
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Configured app.path is not a file: " + path);
        }
        return path.toString();
    }
}
