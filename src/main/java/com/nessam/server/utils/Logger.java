package com.nessam.server.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

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
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    private boolean includeTimestamp;
    private boolean useStyles;

    public Logger() {
        this.includeTimestamp = true;
        this.useStyles = true;
    }

    public void log(LogLevel level, String message) {
        String color;
        switch (level) {
            case DEBUG:
                color = CYAN;
                break;
            case INFO:
                color = GREEN;
                break;
            case WARNING:
                color = YELLOW;
                break;
            case ERROR:
                color = RED;
                break;
            case CRITICAL:
                color = BOLD + RED;
                break;
            default:
                color = RESET;
        }

        String timestamp = "";
        if (includeTimestamp) {
            timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ";
        }

        String style = useStyles ? BOLD : "";


        String classname[] = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");


        System.out.println(color + style + timestamp + "[" + level + "] " + classname[classname.length - 1] + " " +
                Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + message + RESET);
    }


}
