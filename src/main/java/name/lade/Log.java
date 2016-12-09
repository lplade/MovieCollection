package name.lade;

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


    public void debug(String message) {
        if (LOG_LEVEL >= 0) {
            System.out.println("DEBUG: " + message);
        }
    }

    public void info(String message) {
        if (LOG_LEVEL >= 1) {
            System.out.println("INFO: " + message);
        }
    }

    public void warn(String message) {
        if (LOG_LEVEL >= 2) {
            System.out.println("WARN: " + message);
        }
    }

    public void error(String message) {
        if (LOG_LEVEL >= 3) {
            System.err.println("ERROR: " + message);
        }
    }

    public void fatal(String message) {
        if (LOG_LEVEL >= 4) {
            System.err.println("FATAL: " + message);
        }
    }

}
