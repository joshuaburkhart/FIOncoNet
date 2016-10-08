/**
 * Created by burkhart on 6/16/16.
 */
public enum LoggingLevel {
    INFO(0),
    WARNING(1),
    ERROR(2),
    FATAL(3);

    private Integer severity;

    LoggingLevel(int severity) {
        this.severity = severity;
    }

    public boolean ge(LoggingLevel other) {
        return this.severity >= other.severity;
    }
}
