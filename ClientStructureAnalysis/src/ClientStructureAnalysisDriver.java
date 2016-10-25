import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import java.io.IOException;
import java.util.*;

/**
 * Created by burkhart on 10/8/16.
 */
public class ClientStructureAnalysisDriver {
    final String LOG_FILE_PATH = "log/ClientStructureAnalysis.log.txt";
    final String STRUCT_DIR = "/home/burkhart/Software/FIOncoNet/DockingServer/output/execution_1475286571142/";
    final String REACTION = "reaction_5672965";
    final String IN_DIR = "/home/burkhart/Software/FIOncoNet/DockingServer/input/" + REACTION + "/";
    final String PDB_DIR = IN_DIR + "pdb/";
    final String DOCKING_SCHEDULE = IN_DIR + "docking_schedule.xlsx";
    final String UNIPROT_PDB_MAP = IN_DIR + "model_data.dat";
    final String DOCKING_SCHEDULE_SHEET = "WithComplex";
    final int UNIPROT_1_IDX = 0;
    final int UNIPROT_2_IDX = 1;
    final int GENE_1_IDX = 2;
    final int GENE_2_IDX = 3;
    final int POS_FEAT_IDX = 4;

    IReportGenerator reportGenerator;
    ILogger logger;

    public ClientStructureAnalysisDriver(String[] args) {
        this.logger = new Logger(LOG_FILE_PATH,LoggingLevel.INFO);
        this.reportGenerator = new ReportGenerator(null,this.logger);
    }

    public void run() {
        //parse uniprot-pdb map
        IDataSource uniprotPdbMap = new UniprotPdbMapFile(
                UNIPROT_PDB_MAP,
                this.logger);
        IDataSourceReader uniprotPdbMapFileParser = new UniprotPdbMapFileParser(
                this.logger,
                PDB_DIR);
        Collection<IEntity> modeled_genes = uniprotPdbMapFileParser.Read(uniprotPdbMap);

        //reorganize into map
        Map<String, IGene> modeled_genes_map = new HashMap<>();
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
                STRUCT_DIR);
        Collection<IEntity> scheduled_jobs = dockingScheduleFileParser.Read(dockingSchedule);

        List<IEntity> structureInterfaces = new ArrayList<>();
        //each docking job
        for(IEntity entity:scheduled_jobs) {
            IDockingJob job = (IDockingJob) entity;

            for (int i = job.getDockingRangeMin(); i <= job.getDockingRangeMax(); i++) {
                String dockingNumberString = String.format("%03d", i);
                try {
                    Structure structure = StructureIO.getStructure(job.StructureNamePath() +
                                                                   dockingNumberString +
                                                                   "." + job.getDockingFileExt());

                    //extract interface
                    structureInterfaces.add(new StructureInterface(this.logger, structure, job.getShortName() + dockingNumberString));
                } catch (IOException ioe) {
                    this.logger.Log(LoggingLevel.ERROR, "io exception getting structure", ioe);
                } catch (StructureException se) {
                    this.logger.Log(LoggingLevel.ERROR, "structure exception getting structure", se);
                } catch (IllegalArgumentException iae) {
                    this.logger.Log(LoggingLevel.ERROR, "illegal argument getting structure", iae);
                }
            }
        }
        //data like: complex name, res A/B, len A/B, seq A/B, seq length A/B
        this.reportGenerator.WriteEntitiesToFileSystem(structureInterfaces);

        this.logger.StopLogging();
    }
}
