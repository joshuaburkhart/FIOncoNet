package lib;

import java.util.Collection;
import java.util.Map;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IEntityBuilder {
    void AddDisconnectedVariants(IDataSourceReader variantDataSourceReader, IVariantDataSource variantDataSource);
    void AddDisconnectedPairwiseInteractions(IPairwiseInteractionDataSource pairwiseInteractionDataSource);
    Collection<IEntity> GetConnectedVariants();
    Collection<IEntity> GetConnectedGenes();
    Collection<IEntity> GetConnectedIsoforms();
    Collection<IEntity> GetConnectedPairwiseInteractions();
}
