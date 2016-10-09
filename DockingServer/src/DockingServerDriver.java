import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by burkhart on 9/28/16.
 */
public class DockingServerDriver {
    final String REACTION = "reaction_5672965";
    final String IN_DIR = "input/" + REACTION + "/";
    final String OUT_DIR = "output/";
    final String PDB_DIR = IN_DIR + "pdb/";
    final String DOCKING_SCHEDULE = IN_DIR + "docking_schedule.xlsx";
    final String UNIPROT_PDB_MAP = IN_DIR + "model_data.dat";
    final String DOCKING_SCHEDULE_SHEET = "WithComplex";
    final String LOG_FILE_PATH = "log/DockingServer.log.txt";
    final String HEX_EXE_PATH = "/home/burkhart/hex/exe/hex8.0.0-nogui.x64";
    final int UNIPROT_1_IDX = 0;
    final int UNIPROT_2_IDX = 1;
    final int GENE_1_IDX = 2;
    final int GENE_2_IDX = 3;
    final int POS_FEAT_IDX = 4;

    ILogger logger;

    public DockingServerDriver(String[] args) {
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
        this.logger.Log(LoggingLevel.INFO, "Starting execution " + executionStartTime);
        //create output directory for this execution
        java.io.File executionDirectory = new java.io.File(OUT_DIR + "execution_" + executionStartTime);

        // if the directory does not exist, create it
        if (!executionDirectory.exists()) {
            System.out.println("creating directory: " + executionDirectory);
            boolean result = false;

            try {
                executionDirectory.mkdir();
                result = true;
            } catch (SecurityException se) {
                this.logger.Log(LoggingLevel.ERROR, "security exception thrown while creating execution directory", se);
            }
            if (result) {
                this.logger.Log(LoggingLevel.INFO, "created output directory '" + executionDirectory.getPath() + "'");
            }
        }

        //log file & directory paths
        this.logger.Log(LoggingLevel.INFO, "Project Directory: " + System.getProperty("user.dir"));
        this.logger.Log(LoggingLevel.INFO, "Input Directory: " + IN_DIR);
        this.logger.Log(LoggingLevel.INFO, "Output Directory: " + OUT_DIR);
        this.logger.Log(LoggingLevel.INFO, "Pdb directory: " + PDB_DIR);
        this.logger.Log(LoggingLevel.INFO, "Docking Schedule File: " + DOCKING_SCHEDULE);
        this.logger.Log(LoggingLevel.INFO, "Docking Schedule Sheet: " + DOCKING_SCHEDULE_SHEET);
        this.logger.Log(LoggingLevel.INFO, "Uniprot 1 idx: " + UNIPROT_1_IDX);
        this.logger.Log(LoggingLevel.INFO, "Uniprot 2 idx: " + UNIPROT_2_IDX);
        this.logger.Log(LoggingLevel.INFO, "Gene 1 idx: " + GENE_1_IDX);
        this.logger.Log(LoggingLevel.INFO, "Gene 2 idx: " + GENE_2_IDX);
        this.logger.Log(LoggingLevel.INFO, "Pos Feat idx: " + POS_FEAT_IDX);
        this.logger.Log(LoggingLevel.INFO, "Log File: " + LOG_FILE_PATH);
        this.logger.Log(LoggingLevel.INFO, "Hex Exe: " + HEX_EXE_PATH);
        this.logger.Log(LoggingLevel.INFO, "Execution Output Directory: " + executionDirectory);

        //parse uniprot-pdb map
        IDataSource uniprotPdbMap = new UniprotPdbMapFile(
                UNIPROT_PDB_MAP,
                this.logger);
        IDataSourceReader uniprotPdbMapFileParser = new UniprotPdbMapFileParser(
                this.logger,
                PDB_DIR);
        Collection<IEntity> modeled_genes = uniprotPdbMapFileParser.Read(uniprotPdbMap);

        //reorganize into map
        Map<java.lang.String, IGene> modeled_genes_map = new HashMap<>();
        for (IEntity entity : modeled_genes) {
            IGene gene = (IGene) entity;
            modeled_genes_map.put(gene.GetGeneSymbol(), gene);
        }

        //parse docking schedule using libapache-poi-java, see https://poi.apache.org/download.html
        IDataSource dockingSchedule = new DockingScheduleFile(
                DOCKING_SCHEDULE,
                DOCKING_SCHEDULE_SHEET,
                this.logger);
        IDataSourceReader dockingScheduleFileParser = new DockingScheduleFileParser(
                        this.logger,
                        modeled_genes_map,
                        UNIPROT_1_IDX,
                        UNIPROT_2_IDX,
                        GENE_1_IDX,
                        GENE_2_IDX,
                        POS_FEAT_IDX,
                        executionDirectory.getPath());
        Collection<IEntity> scheduled_jobs = dockingScheduleFileParser.Read(dockingSchedule);

        //log schedule
        this.logger.Log(LoggingLevel.INFO, "Docking Schedule:");
        for (IEntity entity : scheduled_jobs) {
            IDockingJob job = (IDockingJob) entity;
            this.logger.Log(LoggingLevel.INFO, job.toString());
        }

        //each protein pair
        for (IEntity entity : scheduled_jobs) {
            IDockingJob job = (IDockingJob) entity;
            long dockingStartTime = System.currentTimeMillis();
            //log protein pair -- include start time
            this.logger.Log(LoggingLevel.INFO, "Preparing to Dock " + job.toString());
            //create output subdirectory for pair
            job.CreateOutputDirectory();

            //create necessary files and execute shell command in foreground (wait for completion)
            job.ExecuteShellScript(job.CreateHexShellScript(HEX_EXE_PATH, job.CreateHexMacro()));

            //log docking finish time
            long dockingFinishTime = System.currentTimeMillis();
            this.logger.Log(LoggingLevel.INFO, "docking took " +
                    (dockingStartTime - dockingFinishTime) / 1000 +
                    " seconds");
        }
        long executionFinishTime = System.currentTimeMillis();
        //log execution finish time
        this.logger.Log(LoggingLevel.INFO, "execution took " +
                (executionStartTime - executionFinishTime) / 1000 +
                " seconds");
        //stop logging
        this.logger.StopLogging();
    }
}



































