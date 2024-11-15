package io.vn.dungxnd.duckabary.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String getFormattedTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
}
