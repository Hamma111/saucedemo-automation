package com.saucedemo.driver;

import com.saucedemo.utils.Log;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Works out which Chrome to drive when none is given explicitly.
 *
 * <p>Selenium Manager finds Google Chrome on its own, but it does not recognise Chromium (the
 * open-source build that Linux distributions ship) and would download a separate Chrome
 * instead. When only Chromium is installed, this class points Selenium at it and at the
 * distribution's matching {@code chromedriver}, if there is one on the {@code PATH}.
 */
final class BrowserLocator {

    private static final List<String> GOOGLE_CHROME_COMMANDS =
            List.of("google-chrome", "google-chrome-stable", "chrome");
    private static final List<String> CHROMIUM_COMMANDS =
            List.of("chromium", "chromium-browser");
    private static final String CHROMEDRIVER_PROPERTY = "webdriver.chrome.driver";

    // Looked up once per JVM; every browser start reuses the answer.
    private static final Optional<Path> CHROMIUM = detectChromium();

    private BrowserLocator() {
    }

    /**
     * Returns the Chromium executable to use, or empty when Selenium Manager should pick the
     * browser itself (the normal case on machines with Google Chrome installed).
     */
    static Optional<Path> chromiumFallback() {
        return CHROMIUM;
    }

    private static Optional<Path> detectChromium() {
        if (findOnPath(GOOGLE_CHROME_COMMANDS).isPresent()) {
            return Optional.empty();
        }
        Optional<Path> chromium = findOnPath(CHROMIUM_COMMANDS);
        chromium.ifPresent(path -> {
            Log.info("Google Chrome not found; using Chromium at " + path);
            useMatchingChromedriver();
        });
        return chromium;
    }

    /** Prefers the distribution's chromedriver, which is built to match its Chromium. */
    private static void useMatchingChromedriver() {
        if (System.getProperty(CHROMEDRIVER_PROPERTY) != null) {
            return;
        }
        findOnPath(List.of("chromedriver")).ifPresent(driver -> {
            Log.info("Using chromedriver at " + driver);
            System.setProperty(CHROMEDRIVER_PROPERTY, driver.toString());
        });
    }

    private static Optional<Path> findOnPath(List<String> commands) {
        String pathVariable = System.getenv("PATH");
        if (pathVariable == null) {
            return Optional.empty();
        }
        for (String directory : pathVariable.split(File.pathSeparator)) {
            for (String command : commands) {
                Path candidate = Path.of(directory, command);
                if (Files.isExecutable(candidate) && !Files.isDirectory(candidate)) {
                    return Optional.of(candidate);
                }
            }
        }
        return Optional.empty();
    }
}
