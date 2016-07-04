package lib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

/**
 * Created by burkhart on 6/16/16.
 */
public class ReportGenerator implements IReportGenerator {
    private ILogger logger;
    private String reportFilePath;
    private String reportText;
    private PrintWriter printWriter;

    public ReportGenerator(String reportFilePath, ILogger logger){
        this.logger = logger;
        this.reportFilePath = reportFilePath != null
                ? reportFilePath
                : "reports/" + this.logger.LongTimeStamp() + ".txt";
        this.reportText = "";
    }

    @Override
    public void WriteEntitiesToFileSystem(Collection<IEntity> entities) {
        for(IEntity entity : entities){
            this.reportText += entity.ToString();
        }
        try{
            this.printWriter = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter(this.reportFilePath,true)));
        }catch(IOException ioe){
            this.logger.Log(LoggingLevel.ERROR,"Could not open report file '" +
                    this.reportFilePath + "'.",ioe);
        }
        this.printWriter.print(reportText);
        this.printWriter.flush();
        this.printWriter.close();
    }
}
