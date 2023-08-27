package log;

import java.util.Arrays;
import java.util.List;

public class Log {
    public static final String OFF = "OFF";
    public static final String SEVERE = "SEVERE";
    public static final String WARNING = "WARNING";
    public static final String INFO = "INFO";
    public static final String CONFIG = "CONFIG";
    public static final String FINE = "FINE";
    public static final String FINER = "FINER";
    public static final String FINEST = "FINEST";
    public static final String ALL = "ALL";

    // convenience variables
    public static final List<String> LEVELS = Arrays.asList(
            OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL);
}
