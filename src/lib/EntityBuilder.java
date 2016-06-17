package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public class EntityBuilder implements IEntityBuilder {
    @Override
    public void AddVariants(Collection<IVariant> variants) {

    }

    @Override
    public void AddPairwiseInteractionDatabase(IExternalDatabase database) {

    }

    /**
     *
     * @return a collection of genes, each supported by at least one variant
     */
    @Override
    public Collection<IEntity> GetSupportedGenes() {
        return null;
    }

    /**
     *
     * @return a collection of isoforms, each supported by at least one gene
     */
    @Override
    public Collection<IEntity> GetSupportedIsoforms() {
        return null;
    }

    /**
     *
     * @return a collection of pairwise interactions, each supported by at least one isoform
     */
    @Override
    public Collection<IEntity> GetSupportedPairwiseInteractions() {
        return null;
    }

    private static IEntityBuilder entityBuilder = null;

    public static IEntityBuilder Instance() {
        if(entityBuilder == null){
            entityBuilder = new EntityBuilder();
        }
        return entityBuilder;
    }
}
