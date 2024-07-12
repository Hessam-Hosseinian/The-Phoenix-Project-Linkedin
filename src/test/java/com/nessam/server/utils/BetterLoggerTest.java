package com.nessam.server.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BetterLoggerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testDebugLogging() {
        BetterLogger.DEBUG("This is a debug message");
        assertLogMessageContains("DEBUG", "This is a debug message", BetterLogger.CYAN);
    }

    @Test
    public void testInfoLogging() {
        BetterLogger.INFO("This is an info message");
        assertLogMessageContains("INFO", "This is an info message", BetterLogger.GREEN);
    }

    @Test
    public void testWarningLogging() {
        BetterLogger.WARNING("This is a warning message");
        assertLogMessageContains("WARNING", "This is a warning message", BetterLogger.YELLOW);
    }

    @Test
    public void testErrorLogging() {
        BetterLogger.ERROR("This is an error message");
        assertLogMessageContains("ERROR", "This is an error message", BetterLogger.RED);
    }

    @Test
    public void testCriticalLogging() {
        BetterLogger logger = new BetterLogger();
        logger.CRITICAL("This is a critical message");
        assertLogMessageContains("CRITICAL", "This is a critical message", BetterLogger.BOLD + BetterLogger.RED);
    }

    private void assertLogMessageContains(String logLevel, String message, String color) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        String expectedClassName = this.getClass().getSimpleName();
        String expectedMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        String logOutput = outContent.toString();
        assertTrue(logOutput.contains(color + formattedDateTime));
        assertTrue(logOutput.contains("[" + logLevel + "]"));
        assertTrue(logOutput.contains(expectedClassName));
        assertTrue(logOutput.contains(expectedMethodName));
        assertTrue(logOutput.contains(message));
    }
}
