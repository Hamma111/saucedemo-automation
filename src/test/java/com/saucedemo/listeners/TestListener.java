package com.saucedemo.listeners;

import com.saucedemo.tests.BaseTest;
import com.saucedemo.utils.BrowserUtils;
import com.saucedemo.utils.Log;
import java.nio.file.Path;
import java.util.Arrays;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Writes a start/pass/fail line per test and saves a screenshot when a test, or one of its
 * setup methods, fails.
 */
public class TestListener implements ITestListener, IConfigurationListener {

    @Override
    public void onTestStart(ITestResult result) {
        Log.info("START   " + describe(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Log.info("PASSED  " + describe(result) + " (" + durationMs(result) + " ms)");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Log.error("FAILED  " + describe(result), result.getThrowable());
        saveScreenshot(result);
    }

    @Override
    public void beforeConfiguration(ITestResult result) {
        Log.info("SETUP   " + describe(result));
    }

    /** A failing setup step (for example login) skips its tests, so capture the page here. */
    @Override
    public void onConfigurationFailure(ITestResult result) {
        Log.error("SETUP FAILED  " + describe(result), result.getThrowable());
        saveScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Log.info("SKIPPED " + describe(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        Log.info(String.format(
                "FINISHED %s: %d passed, %d failed, %d skipped, %d setup failures",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size(),
                context.getFailedConfigurations().size()));
    }

    private static void saveScreenshot(ITestResult result) {
        if (!(result.getInstance() instanceof BaseTest)) {
            return;
        }
        BaseTest test = (BaseTest) result.getInstance();
        if (test.getDriver() != null) {
            // Class name included: parallel classes share setup method names such as "login".
            String fileName = result.getTestClass().getRealClass().getSimpleName()
                    + "." + result.getMethod().getMethodName() + "-" + System.currentTimeMillis();
            Path screenshot = BrowserUtils.takeScreenshot(test.getDriver(), fileName);
            Log.info("Screenshot saved to " + screenshot);
        }
    }

    private static String describe(ITestResult result) {
        String name = result.getTestClass().getRealClass().getSimpleName()
                + "." + result.getMethod().getMethodName();
        Object[] parameters = result.getParameters();
        return parameters.length == 0 ? name : name + Arrays.toString(parameters);
    }

    private static long durationMs(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
