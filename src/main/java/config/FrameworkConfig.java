package config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

public final class FrameworkConfig {
    private static final String CONFIG_PROPERTY = "appium.config";
    private static final Properties PROPERTIES = loadProperties();

    private FrameworkConfig() {
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        String configuredPath = System.getProperty(CONFIG_PROPERTY);
        try {
            if (configuredPath != null && !configuredPath.isBlank()) {
                try (InputStream input = Files.newInputStream(Path.of(configuredPath))) {
                    properties.load(input);
                }
            } else {
                try (InputStream input = FrameworkConfig.class.getClassLoader()
                        .getResourceAsStream("config.properties")) {
                    if (input == null) {
                        throw new IllegalStateException("config.properties was not found on the classpath.");
                    }
                    properties.load(input);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Appium framework configuration.", e);
        }
        return properties;
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = PROPERTIES.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required configuration property is missing: " + key);
        }
        return value.trim();
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null) {
            value = PROPERTIES.getProperty(key);
        }
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    public static String platform() {
        return get("platform").toLowerCase(Locale.ROOT);
    }

    public static String appiumServerUrl() {
        return get("appium.server.url");
    }

    public static String deviceName() {
        return get("device.name");
    }

    public static String appPath() {
        return getOrDefault("app.path", "");
    }

    public static String androidAppPackage() {
        return get("android.app.package");
    }

    public static String androidAppActivity() {
        return get("android.app.activity");
    }

    public static String calculatorAppPackage() {
        return get("calculator.app.package");
    }

    public static String calculatorAppActivity() {
        return get("calculator.app.activity");
    }

    public static String calculatorFirstNumber() {
        return getOrDefault("calculator.first.number", "12");
    }

    public static String calculatorSecondNumber() {
        return getOrDefault("calculator.second.number", "4");
    }

    public static String iosBundleId() {
        return getOrDefault("ios.bundle.id", "");
    }

    public static String browserName() {
        return getOrDefault("browser.name", platform().equals("ios") ? "Safari" : "Chrome");
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(getOrDefault("wait.timeout.seconds", "15"));
    }

    public static Path reportPath() {
        return Path.of(getOrDefault("report.path", "target/reports/extent-report.html"));
    }
}
