package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IObjectFactory {
    Collection<IGene> BuildGenes(Collection<IVariant> variants);

    Collection<IInteractionComplex> BuildInteractionComplexes(Collection<IGene> genes);
}
