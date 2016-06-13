import lib.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by burkhart on 6/12/16.
 */
public class Driver {

    ExecutionState execState;
    IExecutionStateModel executionStateModel;
    IOptionParser optionParser;
    IVariantFileParser variantFileParser;
    IObjectFactory objectFactory;
    Collection<IVariantFile> variantFiles;

    public Driver(String[] args){
        /*
        options = optionParser.parse(args)
        this.execState = executionStateModel.state(options.studies, options.variant_files)
         */
    }

    public void run(){
        switch(this.execState){
            case SingleGroup:
                Collection<IVariant> variants = new HashSet<IVariant>();
                Collection<IGene> genes = new HashSet<IGene>();
                Collection<IIsoform> isoforms = new HashSet<IIsoform>();
                Collection<IPairwiseInteraction> pairwiseInteractions = new HashSet<IPairwiseInteraction>();

                // parse file
                for(IVariantFile variantFile : variantFiles) {
                    variants.addAll(variantFileParser.ParseVariantsFromFile(variantFile));
                }

                // build nodes
                genes.addAll(objectFactory.BuildGenes(variants));
                isoforms.addAll(objectFactory.BuildIsoforms(genes));
                pairwiseInteractions.addAll(objectFactory.BuildPairwiseInteractions(isoforms));

                // apply a scoring function

                // filter

                // write results

                break;
            default:
                break;
        }
    }
}
