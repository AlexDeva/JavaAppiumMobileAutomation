package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import config.FrameworkConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExtentReportManager {
    private static ExtentReports reports;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static synchronized void initialize() {
        if (reports != null) {
            return;
        }
        Path reportPath = FrameworkConfig.reportPath().toAbsolutePath().normalize();
        try {
            Path reportDirectory = reportPath.getParent();
            if (reportDirectory != null) {
                Files.createDirectories(reportDirectory);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create report directory: " + reportPath.getParent(), e);
        }
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath.toString());
        reports = new ExtentReports();
        reports.attachReporter(reporter);
        reports.setSystemInfo("Platform", FrameworkConfig.platform());
        reports.setSystemInfo("Device", FrameworkConfig.deviceName());
    }

    public static synchronized ExtentTest createTest(String name) {
        if (reports == null) {
            initialize();
        }
        ExtentTest test = reports.createTest(name);
        TEST.set(test);
        return test;
    }

    public static ExtentTest currentTest() {
        return TEST.get();
    }

    public static synchronized void flush() {
        if (reports != null) {
            reports.flush();
        }
    }

    public static void clearTest() {
        TEST.remove();
    }
}
