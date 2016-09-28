package src;

import lib.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.examples.IterateCells;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by burkhart on 9/28/16.
 */
public class Driver {
    final String PROJECT_DIR = "/home/burkhart/Software/FIOncoNet/DockingServer/";
    final String IN_DIR = PROJECT_DIR + "input/";
    final String OUT_DIR = PROJECT_DIR + "output/";
    final String PDB_DIR = IN_DIR + "pdb/";
    final String DOCKING_SCHEDULE = IN_DIR + "docking_schedule.xlsx";
    final String DOCKING_SCHEDULE_SHEET = "WithComplex";
    final int UNIPROT_1_IDX = 0;
    final int UNIPROT_2_IDX = 1;
    final int GENE_1_IDX = 2;
    final int GENE_2_IDX = 3;
    final int POS_FEAT_IDX = 4;
    final String LOG_FILE_PATH = OUT_DIR + "log.txt";

    FileParser dockingScheduleParser;
    IFile dockingSchedule;
    ILogger logger;

    public Driver(String[] args){
        /*
        IOptions options = OptionParser.parse(args);
        this.logger = new Logger(options.logging_options);
        this.reportGenerator = new ReportGenerator(options.reporting_options);
        this.execState = ExecutionStateModel.state(options.studies, options.variant_files, options.external_database_files);
        */
        this.logger = new Logger(LOG_FILE_PATH, LoggingLevel.INFO);

    }

    public void run() {
        long executionStartTime = System.currentTimeMillis();
        this.logger.Log(LoggingLevel.INFO,"Starting execution " + executionStartTime);
        //create output directory for this execution
        java.io.File executionDirectory = new java.io.File(OUT_DIR + "execution_" + executionStartTime);

        // if the directory does not exist, create it
        if (!executionDirectory.exists()) {
            System.out.println("creating directory: " + executionDirectory);
            boolean result = false;

            try{
                executionDirectory.mkdir();
                result = true;
            }
            catch(SecurityException se){
               this.logger.Log(LoggingLevel.ERROR,"security exception thrown while creating output directory",se);
            }
            if(result) {
                this.logger.Log(LoggingLevel.INFO,"created output directory '" + executionDirectory + "'");
            }
        }

        //log file & directory paths
        this.logger.Log(LoggingLevel.INFO,"Project Directory: " + PROJECT_DIR);
        this.logger.Log(LoggingLevel.INFO,"Input Directory: " + IN_DIR);
        this.logger.Log(LoggingLevel.INFO,"Output Directory: " + OUT_DIR);
        this.logger.Log(LoggingLevel.INFO,"Pdb directory: " + PDB_DIR);
        this.logger.Log(LoggingLevel.INFO,"Docking Schedule File: " + DOCKING_SCHEDULE);
        this.logger.Log(LoggingLevel.INFO,"Docking Schedule Sheet: " + DOCKING_SCHEDULE_SHEET);
        this.logger.Log(LoggingLevel.INFO,"Uniprot 1 idx: " + UNIPROT_1_IDX);
        this.logger.Log(LoggingLevel.INFO,"Uniprot 2 idx: " + UNIPROT_2_IDX);
        this.logger.Log(LoggingLevel.INFO,"Gene 1 idx: " + GENE_1_IDX);
        this.logger.Log(LoggingLevel.INFO,"Gene 2 idx: " + GENE_2_IDX);
        this.logger.Log(LoggingLevel.INFO,"Pos Feat idx: " + POS_FEAT_IDX);
        this.logger.Log(LoggingLevel.INFO,"Log File: " + LOG_FILE_PATH);
        this.logger.Log(LoggingLevel.INFO,"Execution Output Directory: " + executionDirectory);

        //parse docking schedule using libapache-poi-java, see https://poi.apache.org/download.html
        try {
            InputStream inp = new FileInputStream(DOCKING_SCHEDULE);

            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheet(DOCKING_SCHEDULE_SHEET);
            Iterator rit = sheet.rowIterator();
            while(rit.hasNext()){
                Row row = (Row) rit.next();
                if(row.getCell(POS_FEAT_IDX).getStringCellValue().equals("1")){
                    String uniprot1 = row.getCell(UNIPROT_1_IDX).getStringCellValue();
                    String uniprot2 = row.getCell(UNIPROT_2_IDX).getStringCellValue();
                    String gene1 = row.getCell(GENE_1_IDX).getStringCellValue();
                    String gene2 = row.getCell(GENE_2_IDX).getStringCellValue();
                    this.logger.Log(LoggingLevel.INFO,"parsed xlsx docking: " +
                                                        uniprot1 + ", " +
                                                        uniprot2 + ", " +
                                                        gene1 + ", " +
                                                        gene2 + ", ");
                    Gene g1 = new Gene(gene1,uniprot1);
                    Gene g2 = new Gene(gene2,uniprot2);

                    //create a docking job object -- add to lib that includes an input and output path
                }
            }
        }catch(FileNotFoundException fnf){
            this.logger.Log(LoggingLevel.FATAL,"docking schedule does not exist",fnf);
        }catch(InvalidFormatException ife){
            this.logger.Log(LoggingLevel.FATAL,"docking schedule file has invalid format",ife);
        }catch(IOException io){
            this.logger.Log(LoggingLevel.FATAL,"docking schedule file causes io exception",io);
        }

        //log schedule
        //each protein pair
            long dockingStartTime = System.currentTimeMillis();
            //log protein pair -- include start time
            //create output subdirectory for pair
            //create hex .mac file text
            //log .mac file
            //write .mac file to output directory
            //create shell command
            //log shell command
            //execute shell command in foreground (wait for completion)
            //gzip subdirectory for pair
            //log protein pair gzipped subdirectory path, size, & finish time
            long dockingFinishTime = System.currentTimeMillis();
        long executionFinishTime = System.currentTimeMillis();
        //log finish time
        //copy log file into subdirectory for execution
        //log execution gzipped subdirectory path, size, & finish time
        //gzip subdirectory for execution
        //stop logging
        this.logger.StopLogging();
    }
}



































