package lib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by burkhart on 6/16/16.
 */
public class Logger implements ILogger {
    private static String DEFAULT_LOG_FILE_PATH = "log/FIOncoNet.log.txt";
    private static String NEW_EXECUTION_BANNER = "\n//\n// New FIOncoNet Exection\n//";
    private static String LOG_FIELD_DELIMITER = "\t";
    private LoggingLevel loggingLevel;
    private String logFilePath;
    private PrintWriter printWriter;

    public Logger(String logFilePath, LoggingLevel loggingLevel){
        this.logFilePath = logFilePath != null
                ? logFilePath
                : DEFAULT_LOG_FILE_PATH;
        this.loggingLevel = loggingLevel;
        try{
            this.printWriter = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath,true)));
        }catch(IOException ioe){
           System.out.println(LoggingLevel.ERROR + LOG_FIELD_DELIMITER +
                   "could not open log file '" + this.logFilePath +"'." +
                   ioe.getMessage()+":"+ioe.getLocalizedMessage());
        }
        WriteNewExecutionHeader();
    }

    public LoggingLevel SetLoggingLevel(LoggingLevel loggingLevel){
        this.loggingLevel = loggingLevel;
        return this.loggingLevel;
    }

    private void WriteNewExecutionHeader(){
        AppendToLogFile(NEW_EXECUTION_BANNER);
        AppendToLogFile(StringTimeStamp() + LOG_FIELD_DELIMITER + LongTimeStamp());
    }

    @Override
    public String StringTimeStamp(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    @Override
    public long LongTimeStamp() {return System.currentTimeMillis();}

    private void AppendToLogFile(String line){
        this.printWriter.println(line);
        this.printWriter.flush();
    }

    @Override
    public String Log(LoggingLevel loggingLevel, String message) {
        String logText = null;
        if(loggingLevel.ge(this.loggingLevel)) {
            logText = StringTimeStamp() + LOG_FIELD_DELIMITER +
                    LongTimeStamp() + LOG_FIELD_DELIMITER +
                    loggingLevel + LOG_FIELD_DELIMITER +
                    message;
            AppendToLogFile(logText);
        }
        return logText;
    }

    @Override
    public String Log(LoggingLevel loggingLevel, String message, Exception exception) {
        String logText = null;
        if(loggingLevel.ge(this.loggingLevel)) {
            logText = StringTimeStamp() + this.LOG_FIELD_DELIMITER +
                    LongTimeStamp() + this.LOG_FIELD_DELIMITER +
                    loggingLevel + this.LOG_FIELD_DELIMITER +
                    message + this.LOG_FIELD_DELIMITER +
                    exception.getMessage() + this.LOG_FIELD_DELIMITER +
                    exception.getLocalizedMessage();
            AppendToLogFile(logText);
        }
        return logText;
    }

    @Override
    public void StopLogging() {
        this.printWriter.flush();
        this.printWriter.close();
    }
}
