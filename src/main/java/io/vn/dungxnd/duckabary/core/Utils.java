package io.vn.dungxnd.duckabary.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String getFormattedTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }

    public static LocalDateTime getDateTimeFromString(String time) {
        if (time.contains("(") && time.contains(")")) {
            time = time.substring(time.indexOf("(") + 1, time.indexOf(")"));
        }

        if (time.length() == 16) {
            time = time + ":00";
        }

        if (time.length() == 13) {
            time = time + ":00:00";
        }

        if (time.length() == 10) {
            time = time + " 00:00:00";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
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
