package lib;

/**
 * Created by burkhart on 6/16/16.
 */
public interface ILogger {
    void Log(LoggingLevel level, String message);
    void Log(LoggingLevel level, String message, Exception exception);
    void SetLoggingLevel(LoggingLevel loggingLevel);
    void StopLogging();
    String StringTimeStamp();
    long LongTimeStamp();
}
