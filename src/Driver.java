import lib.*;

import java.util.Collection;

/**
 * Created by burkhart on 6/12/16.
 */
public class Driver {

    ExecutionState execState;
    IVariantFileParser variantFileParser;
    Collection<IVariantFile> variantFiles;
    Collection<IExternalDatabase> externalDatabaseFiles;
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
                Collection<IEntity> genes;
                Collection<IEntity> isoforms;
                Collection<IEntity> pairwiseInteractions;

                this.logger.Log(LoggingLevel.INFO,"add variants to builder");
                for(IVariantFile variantFile : variantFiles) {
                    EntityBuilder.Instance().AddVariants(variantFileParser.ParseVariantsFromFile(variantFile));
                }

                this.logger.Log(LoggingLevel.INFO,"add database to builder");
                for(IExternalDatabase externalDatabaseFile : externalDatabaseFiles) {
                    EntityBuilder.Instance().AddPairwiseInteractionDatabase(externalDatabaseFile);
                }

                this.logger.Log(LoggingLevel.INFO,"build nodes");
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
                    gene.ApplyScoringFunction(ScoringFunctions.CountChildren);
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
