package log;

import java.util.Arrays;
import java.util.List;

public class Log {
    public static final String SEVERE = "SEVERE";
    public static final String WARNING = "WARNING";
    public static final String INFO = "INFO";
    public static final String CONFIG = "CONFIG";
    public static final String FINE = "FINE";
    public static final String FINER = "FINER";
    public static final String FINEST = "FINEST";

    // convenience variables
    public static final List<String> LEVELS = Arrays.asList(SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST);
}
