package com.portableehr.sdk.util;

/**
 * Created by : yvesleborg
 * Date       : 2019-07-09
 * <p>
 * Copyright Portable Ehr Inc, 2018
 */
public class DebugUtils {

    private static final String SEPARATOR = "\n";

    private DebugUtils() {
    }

    public static String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder buffer = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            buffer.append(element).append(SEPARATOR);
        }
        return buffer.toString();
    }

    public static String formatCurrentStacktrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return formatStackTrace(stackTrace);
    }
}
