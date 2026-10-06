package com.saucedemo.driver;

import com.saucedemo.config.Config;
import com.saucedemo.utils.Log;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Builds a ready-to-use Chrome session. Selenium Manager resolves the driver binary, except on
 * machines that only have Chromium, where {@link BrowserLocator} steps in.
 */
public final class DriverFactory {

    private static final int START_ATTEMPTS = 2;

    private DriverFactory() {
    }

    /**
     * Starts Chrome, retrying once: when several browsers start at the same moment, Chrome
     * occasionally fails to come up ("chrome not reachable"), and a second attempt succeeds.
     */
    public static WebDriver createChrome() {
        ChromeOptions options = chromeOptions();
        SessionNotCreatedException lastFailure = null;
        for (int attempt = 1; attempt <= START_ATTEMPTS; attempt++) {
            try {
                return new ChromeDriver(options);
            } catch (SessionNotCreatedException e) {
                lastFailure = e;
                Log.info("Chrome did not start (attempt " + attempt + " of " + START_ATTEMPTS
                        + "): " + firstLine(e.getMessage()));
            }
        }
        throw lastFailure;
    }

    private static String firstLine(String message) {
        return message == null ? "" : message.strip().split("\n")[0];
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000", "--no-first-run", "--disable-notifications");

        if (!Config.CHROME_BINARY.isEmpty()) {
            options.setBinary(Config.CHROME_BINARY);
        } else {
            BrowserLocator.chromiumFallback().ifPresent(path -> options.setBinary(path.toFile()));
        }

        if (Config.isHeadless()) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }

        // The demo password is in public breach lists; without these prefs Chrome shows a
        // "change your password" dialog that blocks the page.
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        return options;
    }
}
