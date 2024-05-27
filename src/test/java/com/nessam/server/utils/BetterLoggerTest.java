package com.nessam.server.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testDebugLog() {
        BetterLogger.DEBUG("Debug message");
        String output = outContent.toString().trim();
        assertLogOutput(output, "DEBUG", "Debug message", BetterLogger.CYAN);
    }

    @Test
    public void testInfoLog() {
        BetterLogger.INFO("Info message");
        String output = outContent.toString().trim();
        assertLogOutput(output, "INFO", "Info message", BetterLogger.GREEN);
    }

    @Test
    public void testWarningLog() {
        BetterLogger.WARNING("Warning message");
        String output = outContent.toString().trim();
        assertLogOutput(output, "WARNING", "Warning message", BetterLogger.YELLOW);
    }

    @Test
    public void testErrorLog() {
        BetterLogger.ERROR("Error message");
        String output = outContent.toString().trim();
        assertLogOutput(output, "ERROR", "Error message", BetterLogger.RED);
    }

    @Test
    public void testCriticalLog() {
        BetterLogger logger = new BetterLogger();
        logger.CRITICAL("Critical message");
        String output = outContent.toString().trim();
        assertLogOutput(output, "CRITICAL", "Critical message", BetterLogger.BOLD + BetterLogger.RED);
    }

    private void assertLogOutput(String output, String level, String message, String color) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String currentDateTime = LocalDateTime.now().format(formatter);

        String[] outputParts = output.split(" ");
        assertTrue(outputParts[0].contains(currentDateTime), "Timestamp should be present");

        String expectedOutput = String.format("%s%s [%s] %s: %s%s",
                color, currentDateTime, level, getClass().getSimpleName(), message, BetterLogger.RESET);

        // Extract the relevant part of the output after the timestamp
        String relevantOutput = output.substring(output.indexOf("] ") + 2);

        // Remove the class and method names from the expected output for comparison
        String expectedRelevantOutput = expectedOutput.substring(expectedOutput.indexOf("] ") + 2);

        assertTrue(relevantOutput.contains(level), "Log level should be present");
        assertTrue(relevantOutput.contains(message), "Log message should be present");
        assertTrue(output.contains(color), "Color code should be present");
        assertTrue(output.contains(BetterLogger.RESET), "Reset code should be present");
    }
}
