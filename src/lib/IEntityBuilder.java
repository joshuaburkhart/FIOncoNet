package lib;

import java.util.Collection;
import java.util.Map;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IEntityBuilder {
    void AddVariants(Collection<IVariant> variants);
    void AddPairwiseInteractionDatabase(IExternalDatabase database);
    Collection<IEntity> GetSupportedGenes();
    Collection<IEntity> GetSupportedIsoforms();
    Collection<IEntity> GetSupportedPairwiseInteractions();
}
