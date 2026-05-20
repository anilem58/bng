package com.blissandglow.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    // Display format: "25 Jan 2024"
    private static final DateTimeFormatter DATE_FMT     = DateTimeFormatter.ofPattern("dd MMM yyyy");

    // Display format: "25 Jan 2024, 14:30"
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    // HTML date input format: "2024-01-25"
    private static final DateTimeFormatter INPUT_FMT    = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtil() {}

    // Format a date for display (e.g. on a page)
    public static String format(LocalDate date) {
        return date == null ? "" : date.format(DATE_FMT);
    }

    // Format a date-time for display
    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATETIME_FMT);
    }

    // Parse the value from an HTML <input type="date"> field
    public static LocalDate parseInput(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(value.trim(), INPUT_FMT);
        } catch (Exception e) {
            return null;
        }
    }

    // Format a date for use in an HTML <input type="date"> field
    public static String toInputValue(LocalDate date) {
        return date == null ? "" : date.format(INPUT_FMT);
    }
}