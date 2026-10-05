package com.saucedemo.listeners;

import com.saucedemo.tests.BaseTest;
import com.saucedemo.utils.BrowserUtils;
import com.saucedemo.utils.Log;
import java.nio.file.Path;
import java.util.Arrays;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Writes a start/pass/fail line per test and saves a screenshot when a test fails. */
public class TestListener implements ITestListener {

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

        if (result.getInstance() instanceof BaseTest test && test.getDriver() != null) {
            String fileName = result.getMethod().getMethodName() + "-" + System.currentTimeMillis();
            Path screenshot = BrowserUtils.takeScreenshot(test.getDriver(), fileName);
            Log.info("Screenshot saved to " + screenshot);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Log.info("SKIPPED " + describe(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        Log.info(String.format(
                "FINISHED %s: %d passed, %d failed, %d skipped",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size()));
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
