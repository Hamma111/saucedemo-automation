package com.saucedemo.driver;

import com.saucedemo.config.Config;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Builds a ready-to-use Chrome session. Selenium Manager resolves the driver binary, except on
 * machines that only have Chromium, where {@link BrowserLocator} steps in.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createChrome() {
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

        return new ChromeDriver(options);
    }
}
