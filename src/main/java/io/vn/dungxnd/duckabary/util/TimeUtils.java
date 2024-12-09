package io.vn.dungxnd.duckabary.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtils {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    /**
     * Get the current date and time.
     *
     * @param time the time to format
     * @return String of the formatted time
     */
    public static String getFormattedTime(LocalDateTime time) {
        return time.format(FORMATTER);
    }

    public static String getDurationFromNow(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        long diff = java.time.Duration.between(now, time).toMinutes();
        if (diff < 60) {
            return diff + " minutes";
        } else if (diff < 1440) {
            return diff / 60 + " hours";
        } else {
            return diff / 1440 + " days";
        }
    }

    public static LocalDate getDateFromString(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }

        date = date.contains("(") ? date.substring(date.indexOf("(") + 1, date.indexOf(")")) : date;
        date = date.trim();

        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new IllegalArgumentException("Invalid date format. Must be yyyy-MM-dd");
        }

        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse date: " + date, e);
        }
    }

    /**
     * Get the date time from a string.
     *
     * @param input the input string
     * @return the date time
     */
    public static LocalDateTime getDateTimeFromString(String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }

    /**
     * Check if the given string is a valid date time.
     *
     * @param time the time to check
     * @return true if the time is valid, false otherwise
     */
    public static boolean isValidDateTime(String time) {
        try {
            getDateTimeFromString(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }
}
