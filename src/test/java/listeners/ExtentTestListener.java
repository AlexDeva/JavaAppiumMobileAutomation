package listeners;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import utils.ExtentReportManager;
import utils.ScreenshotUtils;

import java.io.IOException;
import java.nio.file.Path;

public class ExtentTestListener implements ITestListener, ISuiteListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtentTestListener.class);

    @Override
    public void onStart(ISuite suite) {
        ExtentReportManager.initialize();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentReportManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        currentTest().pass("Test passed.");
        ExtentReportManager.clearTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = currentTest();
        test.fail(result.getThrowable());
        if (BaseTest.currentDriver() != null) {
            try {
                Path screenshot = ScreenshotUtils.capture(BaseTest.currentDriver(),
                        result.getMethod().getMethodName());
                test.addScreenCaptureFromPath(screenshot.toAbsolutePath().toString());
            } catch (IOException | WebDriverException e) {
                LOGGER.error("Unable to capture failure screenshot for {}.", result.getName(), e);
                test.warning("Failure screenshot could not be captured: " + e.getMessage());
            }
        }
        ExtentReportManager.clearTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getThrowable() == null) {
            currentTest().skip("Test skipped.");
        } else {
            currentTest().skip(result.getThrowable());
        }
        ExtentReportManager.clearTest();
    }

    private ExtentTest currentTest() {
        ExtentTest test = ExtentReportManager.currentTest();
        if (test == null) {
            throw new IllegalStateException("No Extent test is associated with the current test thread.");
        }
        return test;
    }
}
