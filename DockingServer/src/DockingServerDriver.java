
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.io.*;
import java.util.*;

import static org.apache.poi.ss.usermodel.WorkbookFactory.*;

/**
 * Created by burkhart on 9/28/16.
 */
public class DockingServerDriver {
    final String REACTION = "reaction_5672965";
    final String IN_DIR = "input/" + REACTION + "/";
    final String OUT_DIR = "output/";
    final String PDB_DIR = IN_DIR + "pdb/";
    final String DOCKING_SCHEDULE = IN_DIR + "docking_schedule.xlsx";
    final java.lang.String UNIPROT_PDB_MAP = IN_DIR + "model_data.dat";
    final String DOCKING_SCHEDULE_SHEET = "WithComplex";
    final int UNIPROT_1_IDX = 0;
    final int UNIPROT_2_IDX = 1;
    final int GENE_1_IDX = 2;
    final int GENE_2_IDX = 3;
    final int POS_FEAT_IDX = 4;
    final String LOG_FILE_PATH = OUT_DIR + "DockingServer.log.txt";
    final String HEX_EXE_PATH = "/home/burkhart/hex/exe/hex8.0.0-nogui.x64";

    ILogger logger;

    public DockingServerDriver(String[] args){
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
               this.logger.Log(LoggingLevel.ERROR,"security exception thrown while creating execution directory",se);
            }
            if(result) {
                this.logger.Log(LoggingLevel.INFO,"created output directory '" + executionDirectory.getPath() + "'");
            }
        }

        //log file & directory paths
        this.logger.Log(LoggingLevel.INFO,"Project Directory: " + System.getProperty("user.dir"));
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
        this.logger.Log(LoggingLevel.INFO,"Hex Exe: " + HEX_EXE_PATH);
        this.logger.Log(LoggingLevel.INFO,"Execution Output Directory: " + executionDirectory);

        //parse uniprot-pdb map
        Collection<IIsoform> modeled_isoforms = new HashSet<>();
        Map<java.lang.String,IGene> modeled_genes = new HashMap<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(UNIPROT_PDB_MAP));
            java.lang.String line;
            while((line = br.readLine()) != null){
               java.lang.String[] toks = line.split("\\s");
                java.lang.String geneSymbol = toks[0].toUpperCase().trim();
                IGene g = new Gene(geneSymbol);
                IIsoform i = new Isoform(g,PDB_DIR + toks[13]);
                g.SetPrincipleIsoform(i);
                modeled_genes.put(g.GetGeneSymbol(),g);
                modeled_isoforms.add(i);
            }
        }catch(FileNotFoundException fnf){
            this.logger.Log(LoggingLevel.FATAL,"uniprot-pdb map does not exist",fnf);
        }catch(IOException io){
            this.logger.Log(LoggingLevel.FATAL,"uniprot-pdb map file causes io exception",io);
        }

        //parse docking schedule using libapache-poi-java, see https://poi.apache.org/download.html
        Collection<IDockingJob> scheduled_jobs = new HashSet<>();
        try {
            InputStream inp = new FileInputStream(DOCKING_SCHEDULE);

            Workbook wb = create(inp);
            Sheet sheet = wb.getSheet(DOCKING_SCHEDULE_SHEET);
            Iterator rit = sheet.rowIterator();
            while(rit.hasNext()){
                Row row = (Row) rit.next();
                if(row.getCell(POS_FEAT_IDX).getCellTypeEnum() == CellType.BOOLEAN){
                    String uniprot1 = row.getCell(UNIPROT_1_IDX).getStringCellValue();
                    String uniprot2 = row.getCell(UNIPROT_2_IDX).getStringCellValue();
                    String gene1 = row.getCell(GENE_1_IDX).getStringCellValue();
                    String gene2 = row.getCell(GENE_2_IDX).getStringCellValue();
                    this.logger.Log(LoggingLevel.INFO,"parsed xlsx docking: " +
                                                        uniprot1 + ", " +
                                                        uniprot2 + ", " +
                                                        gene1 + ", " +
                                                        gene2);
                    //ignore gene1 & 2 for now.. perhaps add these to a
                    //yet unimplemented GeneID class
                    String geneSymbol1 = uniprot1.toUpperCase().trim();
                    String geneSymbol2 = uniprot2.toUpperCase().trim();
                    if(modeled_genes.containsKey(geneSymbol1)){
                        if(modeled_genes.containsKey(geneSymbol2)) {
                            scheduled_jobs.add(new DockingJob(
                                    modeled_genes.get(geneSymbol1).GetPrincipleIsoform(),
                                    modeled_genes.get(geneSymbol2).GetPrincipleIsoform()));
                        }else{
                            this.logger.Log(LoggingLevel.ERROR,"can't find a model for " + geneSymbol2);
                        }
                    }else{
                        this.logger.Log(LoggingLevel.ERROR,"can't find a model for " + geneSymbol1);
                    }
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
        this.logger.Log(LoggingLevel.INFO,"Docking Schedule:");
        for (IDockingJob job: scheduled_jobs) {
            this.logger.Log(LoggingLevel.INFO,job.toString());
        }

        //each protein pair
        for (IDockingJob job: scheduled_jobs) {
            long dockingStartTime = System.currentTimeMillis();
            //log protein pair -- include start time
            this.logger.Log(LoggingLevel.INFO, "Preparing to Dock " + job.toString());
            //create output subdirectory for pair
            java.io.File dockingDirectory = new java.io.File(executionDirectory.getPath() + "/" + job.shortName());

            // if the directory does not exist, create it
            if (!dockingDirectory.exists()) {
                System.out.println("creating directory: " + dockingDirectory);
                boolean result = false;

                try{
                    dockingDirectory.mkdir();
                    result = true;
                }
                catch(SecurityException se){
                    this.logger.Log(LoggingLevel.ERROR,"security exception thrown while creating docking directory",se);
                }
                if(result) {
                    this.logger.Log(LoggingLevel.INFO,"created output directory '" + dockingDirectory.getPath() + "'");
                }
            }
            //create hex .mac file text
            String macText = "open_receptor " + job.getIsoformR().GetPdbPath() + "\n" +
                             "open_ligand " + job.getIsoformL().GetPdbPath() + "\n" +
                             "activate_docking\n" +
                             "save_both " + dockingDirectory.getPath() + "/" +
                                            job.shortName() + ".pdb.gz\n";
            //log .mac file
            this.logger.Log(LoggingLevel.INFO,"Writing Macro:");
            this.logger.Log(LoggingLevel.INFO,macText);
            //write .mac file to output directory
            String macPath = dockingDirectory.getPath() + "/hex_macro.mac";
            try(  PrintWriter out = new PrintWriter(macPath)  ){
                out.println(macText);
            }catch(FileNotFoundException fnf){
                this.logger.Log(LoggingLevel.FATAL,"can't write macro file",fnf);
            }
            //set hex log path
            String hexLogPath = dockingDirectory.getPath() + "/hex_log.txt";
            //create shell command
            String shellText = HEX_EXE_PATH + " -ncpu 4 <" + macPath + " >" + hexLogPath;
            //log shell command
            this.logger.Log(LoggingLevel.INFO,"Writing Shell Command: " + shellText);
            String shellPath = dockingDirectory.getPath() + "/hex_shell.sh";
            //write shell file to output directory
            try(  PrintWriter out = new PrintWriter(shellPath)  ){
                out.println(shellText);
            }catch(FileNotFoundException fnf){
                this.logger.Log(LoggingLevel.FATAL,"can't write shell file",fnf);
            }
            //execute shell command in foreground (wait for completion)
            try {
                ProcessBuilder pb = new ProcessBuilder(shellPath);
                Process p = pb.start();     // Start the process.
                p.waitFor();                // Wait for the process to finish.
                this.logger.Log(LoggingLevel.INFO,"docking complete.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //log docking finish time
            long dockingFinishTime = System.currentTimeMillis();
            this.logger.Log(LoggingLevel.INFO,"docking took " +
                    (dockingStartTime - dockingFinishTime) / 1000 +
                    " seconds");
        }
        long executionFinishTime = System.currentTimeMillis();
        //log execution finish time
        this.logger.Log(LoggingLevel.INFO,"execution took " +
                (executionStartTime - executionFinishTime) / 1000 +
                " seconds");
        //stop logging
        this.logger.StopLogging();
    }
}



































