import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IEntityNetworkBuilder {
    void AddDisconnectedVariants(IDataSourceReader variantDataSourceReader, IDataSource variantDataSource);

    void AddDisconnectedPairwiseInteractions(IDataSourceReader pairwiseInteractionDataSourceReader, IDataSource pairwiseInteractionDataSource);

    Collection<IEntity> GetConnectedVariants();

    Collection<IEntity> GetConnectedGenes();

    Collection<IEntity> GetConnectedIsoforms();

    Collection<IEntity> GetConnectedPairwiseInteractions();
}
