package lib;

import java.util.Collection;
import java.util.Map;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IObjectFactory {
    Collection<IGene> BuildGenes(Collection<IVariant> variants);
    Collection<IIsoform> BuildIsoforms(Collection<IGene> genes);
    Collection<IPairwiseInteraction> BuildPairwiseInteractions(Collection<IIsoform> isoforms);
}
