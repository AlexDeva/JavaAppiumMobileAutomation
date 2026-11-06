package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private ScreenshotUtils() {
    }

    public static Path capture(AppiumDriver driver, String testName) throws IOException {
        Path directory = Path.of("target", "screenshots");
        Files.createDirectories(directory);
        String safeName = testName.replaceAll("[^A-Za-z0-9._-]", "_");
        Path destination = directory.resolve(safeName + "-" + LocalDateTime.now().format(TIMESTAMP) + ".png");
        Files.copy(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath(),
                destination, StandardCopyOption.REPLACE_EXISTING);
        return destination;
    }
}
