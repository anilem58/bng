package com.blissandglow.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DATE_FMT     = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
    private static final DateTimeFormatter INPUT_FMT    = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtil() {}

    public static String format(LocalDate date) {
        return date == null ? "" : date.format(DATE_FMT);
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATETIME_FMT);
    }

    /** Parse HTML date-input value (yyyy-MM-dd). Returns null if blank or invalid. */
    public static LocalDate parseInput(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try { return LocalDate.parse(value.trim(), INPUT_FMT); }
        catch (Exception e) { return null; }
    }

    /** Format a LocalDate as the HTML input value (yyyy-MM-dd). */
    public static String toInputValue(LocalDate date) {
        return date == null ? "" : date.format(INPUT_FMT);
    }
}
