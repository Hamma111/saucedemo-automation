package com.saucedemo.config;

import java.time.Duration;

/** Central place for run settings. Every value can be overridden with a -D system property. */
public final class Config {

    public static final String BASE_URL = System.getProperty("baseUrl", "https://www.saucedemo.com/");
    public static final String USERNAME = System.getProperty("username", "standard_user");
    public static final String PASSWORD = System.getProperty("password", "secret_sauce");
    public static final Duration TIMEOUT =
            Duration.ofSeconds(Long.parseLong(System.getProperty("timeoutSeconds", "10")));

    private Config() {
    }

    /** Headless when asked for explicitly, or automatically on a CI server. */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"))
                || "true".equalsIgnoreCase(System.getenv("CI"));
    }
}
