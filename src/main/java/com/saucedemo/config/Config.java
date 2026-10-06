package com.saucedemo.config;

import java.time.Duration;

/** Central place for run settings. Every value can be overridden with a -D system property. */
public final class Config {

    public static final String BASE_URL = System.getProperty("baseUrl", "https://www.saucedemo.com/");
    public static final String USERNAME = System.getProperty("username", "standard_user");
    public static final String PASSWORD = System.getProperty("password", "secret_sauce");
    public static final Duration TIMEOUT = Duration.ofSeconds(timeoutSeconds());
    /** Optional path to a Chrome/Chromium executable; empty means "let Selenium find Chrome". */
    public static final String CHROME_BINARY = System.getProperty("chromeBinary", "").trim();

    private Config() {
    }

    private static long timeoutSeconds() {
        String value = System.getProperty("timeoutSeconds", "10");
        try {
            long seconds = Long.parseLong(value.trim());
            if (seconds > 0) {
                return seconds;
            }
        } catch (NumberFormatException e) {
            // Falls through to the warning below.
        }
        System.err.println("Ignoring invalid -DtimeoutSeconds=" + value + "; using 10 seconds.");
        return 10;
    }

    /** Headless when asked for explicitly, or automatically on a CI server. */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"))
                || "true".equalsIgnoreCase(System.getenv("CI"));
    }
}
