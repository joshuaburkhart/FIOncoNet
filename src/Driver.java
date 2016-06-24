import lib.*;

import java.util.Collection;

/**
 * Created by burkhart on 6/12/16.
 */
public class Driver {

    ExecutionState execState;
    Collection<IVariantFile> variantFiles;
    Collection<IPairwiseInteractionDataSource> pairwiseInteractionDataFiles;
    IReportGenerator reportGenerator;
    ILogger logger;

    public Driver(String[] args){
        /*
        IOptions options = OptionParser.parse(args);
        this.logger = new Logger(options.logging_options);
        this.reportGenerator = new ReportGenerator(options.reporting_options);
        this.execState = ExecutionStateModel.state(options.studies, options.variant_files, options.external_database_files);
         */
    }

    public void run(){
        switch(this.execState){
            case SingleGroup:
                Collection<IEntity> variants;
                Collection<IEntity> genes;
                Collection<IEntity> isoforms;
                Collection<IEntity> pairwiseInteractions;

                this.logger.Log(LoggingLevel.INFO,"add variants to builder");
                for(IVariantFile variantFile : variantFiles) {
                    EntityBuilder.Instance().AddVariants(variantFile);
                }

                this.logger.Log(LoggingLevel.INFO,"add database to builder");
                for(IPairwiseInteractionDataSource pairwiseInteractionDataFile : pairwiseInteractionDataFiles) {
                    EntityBuilder.Instance().AddPairwiseInteractions(pairwiseInteractionDataFile);
                }

                this.logger.Log(LoggingLevel.INFO,"build nodes");
                variants = EntityBuilder.Instance().GetVariants();
                genes = EntityBuilder.Instance().GetSupportedGenes();
                isoforms = EntityBuilder.Instance().GetSupportedIsoforms();
                pairwiseInteractions = EntityBuilder.Instance().GetSupportedPairwiseInteractions();

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

                this.logger.Log(LoggingLevel.INFO,"filter");
                pairwiseInteractions = Filter.Instance().TopPercentile(10.0,pairwiseInteractions);

                this.logger.Log(LoggingLevel.INFO,"write results");
                reportGenerator.WriteEntitiesToFileSystem(pairwiseInteractions);

                break;
            default:
                break;
        }
    }
}
