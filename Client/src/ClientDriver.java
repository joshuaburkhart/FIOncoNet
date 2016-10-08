

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by burkhart on 6/12/16.
 */
public class ClientDriver {

    ExecutionState execState;
    Collection<IFile> variantFiles;
    FileParser variantFileParser;
    FileParser pairwiseInteractionFileParser;
    Collection<IFile> pairwiseInteractionFiles;
    IReportGenerator reportGenerator;
    IEntityNetworkBuilder entityNetworkBuilder;
    ILogger logger;
    IFilter filter;

    public ClientDriver(String[] args){
        /*
        IOptions options = OptionParser.parse(args);
        this.logger = new Logger(options.logging_options);
        this.reportGenerator = new ReportGenerator(options.reporting_options);
        this.execState = ExecutionStateModel.state(options.studies, options.variant_files, options.external_database_files);
         */
        this.logger = new Logger(null,LoggingLevel.INFO);
        this.execState = ExecutionState.SingleGroup;
        this.variantFiles = new HashSet<>();
        this.variantFiles.add(new MutationAnnotationFile("data/variants/IDC_lumA_dnaseq.maf",this.logger));
        this.variantFileParser = new MAFFileParser(this.logger);
        this.pairwiseInteractionFiles = new HashSet<>();
        this.pairwiseInteractionFiles.add(new ReactomeFIFile("data/reactome/FIsInGene_031516_with_annotations.txt",this.logger));
        this.pairwiseInteractionFileParser = new ReactomeFIFileParser(this.logger);
        this.entityNetworkBuilder = new EntityNetworkBuilder(this.logger);
        this.reportGenerator = new ReportGenerator(null,this.logger);
        this.filter = new Filter(this.logger);
    }

    public void run(){
        switch(this.execState){
            case SingleGroup:
                Collection<IEntity> variants;
                Collection<IEntity> genes;
                Collection<IEntity> isoforms;
                Collection<IEntity> pairwiseInteractions;

                this.logger.Log(LoggingLevel.INFO,"add variants to builder");
                for(IFile variantFile : variantFiles) {
                    this.entityNetworkBuilder.AddDisconnectedVariants(variantFileParser, variantFile);
                }

                this.logger.Log(LoggingLevel.INFO,"add pairwise interactions to builder");
                for(IFile pairwiseInteractionDataFile : pairwiseInteractionFiles) {
                    this.entityNetworkBuilder.AddDisconnectedPairwiseInteractions(pairwiseInteractionFileParser, pairwiseInteractionDataFile);
                }

                this.logger.Log(LoggingLevel.INFO,"build nodes");
                variants = this.entityNetworkBuilder.GetConnectedVariants();
                this.logger.Log(LoggingLevel.INFO, variants.size() + " variants built");
                genes = this.entityNetworkBuilder.GetConnectedGenes();
                this.logger.Log(LoggingLevel.INFO, variants.size() + " genes built");
                isoforms = this.entityNetworkBuilder.GetConnectedIsoforms();
                this.logger.Log(LoggingLevel.INFO, variants.size() + " isoforms built");
                pairwiseInteractions = this.entityNetworkBuilder.GetConnectedPairwiseInteractions();
                this.logger.Log(LoggingLevel.INFO, variants.size() + " pairwise interactions built");

                this.logger.Log(LoggingLevel.INFO,"apply scoring functions");
                for(IEntity interaction : pairwiseInteractions){
                    interaction.ApplyScoringFunction(ScoringFunctions.ChildScoreSum);
                }
                for(IEntity isoform : isoforms){
                    isoform.ApplyScoringFunction(ScoringFunctions.ChildScoreSum);
                }
                for(IEntity gene : genes){
                    gene.ApplyScoringFunction(ScoringFunctions.ChildScoreSum);
                }
                for(IEntity variant : variants){
                    variant.ApplyScoringFunction(ScoringFunctions.ONE);
                }

                this.logger.Log(LoggingLevel.INFO,"filter and write results");
                reportGenerator.WriteEntitiesToFileSystem(filter.TopPercentile(10.0,pairwiseInteractions));

                break;
            default:
                break;
        }
        this.logger.Log(LoggingLevel.INFO, "execution complete. shutting down");
        this.logger.StopLogging();
    }
}
