package com.saucedemo.utils;

import com.saucedemo.config.Config;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Browser-level helpers that do not belong to any single page. */
public final class BrowserUtils {

    public static final Path SCREENSHOT_DIR = Path.of("target", "screenshots");

    private BrowserUtils() {
    }

    /**
     * Runs an action that opens a new tab, switches to that tab and returns the URL it lands on.
     * Call {@link #closeTabAndReturn} afterwards to get back to the original tab.
     */
    public static String openNewTabAndGetUrl(WebDriver driver, Runnable action) {
        Set<String> handlesBefore = driver.getWindowHandles();
        action.run();

        WebDriverWait wait = new WebDriverWait(driver, Config.TIMEOUT);
        String newHandle = wait.until(d -> d.getWindowHandles().stream()
                .filter(handle -> !handlesBefore.contains(handle))
                .findFirst()
                .orElse(null));
        driver.switchTo().window(newHandle);

        return wait.until(d -> {
            String url = d.getCurrentUrl();
            return url == null || url.isBlank() || url.startsWith("about:") ? null : url;
        });
    }

    public static void closeTabAndReturn(WebDriver driver, String originalHandle) {
        driver.close();
        driver.switchTo().window(originalHandle);
    }

    public static Path takeScreenshot(WebDriver driver, String name) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            Path file = SCREENSHOT_DIR.resolve(name + ".png");
            Files.write(file, ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            return file;
        } catch (IOException | RuntimeException e) {
            Log.error("Could not save screenshot " + name, e);
            return null;
        }
    }
}
