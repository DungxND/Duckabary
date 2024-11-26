package io.vn.dungxnd.duckabary.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static String getFormattedTime(LocalDateTime time) {
        return time.format(FORMATTER);
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

    public static LocalDateTime getDateTimeFromString(String time) {
        if (time == null || time.trim().isEmpty()) {
            throw new IllegalArgumentException("Date time string cannot be null or empty");
        }

        time = time.contains("(") ? time.substring(time.indexOf("(") + 1, time.indexOf(")")) : time;
        time = time.trim();

        if (!time.matches("^\\d{4}-\\d{2}-\\d{2}(\\s+\\d{1,2}(:\\d{1,2})?)?$")) {
            throw new IllegalArgumentException(
                    "Invalid date time format. Must be yyyy-MM-dd [HH[:mm]]");
        }

        String[] parts = time.split("\\s+");
        String date = parts[0];
        String timeComponent = parts.length > 1 ? parts[1] : "00:00";

        if (timeComponent.contains(":")) {
            String[] timeParts = timeComponent.split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);

            if (hours < 0 || hours > 23) {
                throw new IllegalArgumentException("Hours must be between 0 and 23");
            }
            if (minutes < 0 || minutes > 59) {
                throw new IllegalArgumentException("Minutes must be between 0 and 59");
            }
        }

        time =
                switch (time.length()) {
                    case 16 -> time + ":00";
                    case 13 -> time + ":00:00";
                    case 10 -> time + " 00:00:00";
                    case 19 -> time;
                    default -> throw new IllegalArgumentException("Invalid date time format");
                };

        try {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse date time: " + time, e);
        }
    }

    public static boolean isValidDateTime(String time) {
        try {
            getDateTimeFromString(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
