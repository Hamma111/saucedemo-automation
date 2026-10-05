package com.saucedemo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** Thin wrapper over java.util.logging: one line per event, to the console and target/logs. */
public final class Log {

    public static final Path LOG_FILE = Path.of("target", "logs", "test-run.log");
    private static final Logger LOGGER = Logger.getLogger("saucedemo");

    static {
        System.setProperty(
                "java.util.logging.SimpleFormatter.format",
                "%1$tT.%1$tL %4$-7s %5$s%6$s%n");
        LOGGER.setUseParentHandlers(false);
        addHandler(new ConsoleHandler());

        try {
            Files.createDirectories(LOG_FILE.getParent());
            addHandler(new FileHandler(LOG_FILE.toString()));
        } catch (IOException e) {
            LOGGER.warning("File logging disabled: " + e.getMessage());
        }
    }

    private Log() {
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void step(String message) {
        LOGGER.info("  > " + message);
    }

    public static void error(String message, Throwable cause) {
        LOGGER.log(Level.SEVERE, message, cause);
    }

    private static void addHandler(Handler handler) {
        handler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(handler);
    }
}
