/**
 * Log.java
 *
 * This handles console logging.
 *
 * Uses standard Unix log levels
 *
 * 0 = debug
 * 1 = info
 * 2 = warn
 * 3 = error
 * 4 = fatal
 *
 */
public class Log {

    //SET THIS TO CHANGE GLOBAL LOGGING BEHAVIOR
    private static final int LOG_LEVEL = 0;


    void debug(String message) {
        if (LOG_LEVEL >= 0) {
            System.out.println("DEBUG: " + message);
        }
    }

    void info(String message) {
        if (LOG_LEVEL >= 1) {
            System.out.println("INFO: " + message);
        }
    }

    void warn(String message) {
        if (LOG_LEVEL >= 2) {
            System.out.println("WARN: " + message);
        }
    }

    void error(String message) {
        if (LOG_LEVEL >= 3) {
            System.err.println("ERROR: " + message);
        }
    }

    void fatal(String message) {
        if (LOG_LEVEL >= 4) {
            System.err.println("FATAL: " + message);
        }
    }

}
