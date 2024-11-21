package io.vn.dungxnd.duckabary.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggerUtils {
    private static final Logger logger = LoggerFactory.getLogger("Duckabary");
    
    private LoggerUtils() {} // Prevent instantiation
    
    public static void info(String message) {
        logger.info(message);
    }
    
    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
    
    public static void warn(String message) {
        logger.warn(message);
    }
    
    public static void debug(String message) {
        logger.debug(message);
    }
    
    public static void trace(String message) {
        logger.trace(message);
    }
}
