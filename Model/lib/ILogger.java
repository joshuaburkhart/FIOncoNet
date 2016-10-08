/**
 * Created by burkhart on 6/16/16.
 */
public interface ILogger {
    String Log(LoggingLevel level, String message);

    String Log(LoggingLevel level, String message, Exception exception);

    LoggingLevel SetLoggingLevel(LoggingLevel loggingLevel);

    void StopLogging();

    String StringTimeStamp();

    long LongTimeStamp();
}
