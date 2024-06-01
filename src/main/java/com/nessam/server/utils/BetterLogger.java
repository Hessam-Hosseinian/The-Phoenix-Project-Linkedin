package com.nessam.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BetterLogger {

    // ANSI escape codes for text colors and styles
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";

    public enum LogLevel {
        DEBUG, INFO, WARNING, ERROR, CRITICAL
    }


    public BetterLogger() {

    }

    private static void log(LogLevel level, String message) {
        String color = switch (level) {
            case DEBUG -> CYAN;
            case INFO -> GREEN;
            case WARNING -> YELLOW;
            case ERROR -> RED;
            case CRITICAL -> BOLD + RED;
            default -> RESET;
        };

        String formattedDateTime = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        formattedDateTime = LocalDateTime.now().format(formatter);


        String style = "";


        String[] classname = Thread.currentThread().getStackTrace()[3].getClassName().split("\\.");

        System.out.println(color + style + formattedDateTime + " [" + level + "] " + RESET + classname[classname.length - 1] + " " + Thread.currentThread().getStackTrace()[3].getMethodName() + color + ": " + message + RESET);
    }

    public static void DEBUG(String message) {
        log(LogLevel.DEBUG, message);
    }

    public static void INFO(String message) {
        log(LogLevel.INFO, message);
    }

    public static void WARNING(String message) {
        log(LogLevel.WARNING, message);
    }

    public static void ERROR(String message) {
        log(LogLevel.ERROR, message);
    }

    public void CRITICAL(String message) {
        log(LogLevel.CRITICAL, message);
    }
}
//this is a test comment