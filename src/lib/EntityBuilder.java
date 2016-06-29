package lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by burkhart on 6/7/16.
 */
public class EntityBuilder implements IEntityBuilder {
    Collection<IEntity> variants;

    public EntityBuilder(){
        this.variants = new HashSet<IEntity>();
    }

    @Override
    public void AddDisconnectedVariants(IDataSourceReader dataSourceReader, IVariantDataSource variantDataSource) {
        if(variantDataSource instanceof IVariantFile) {
            this.variants.addAll(
                    dataSourceReader.Read(variantDataSource));
        }
    }

    @Override
    public void AddDisconnectedPairwiseInteractions(IPairwiseInteractionDataSource pairwiseInteractionDataSource) {

    }

    /**
     *
     * @return a collection of variants
     */
    @Override
    public Collection<IEntity> GetConnectedVariants() { return this.variants; }

    /**
     *
     * @return a collection of genes, each supported by at least one variant
     */
    @Override
    public Collection<IEntity> GetConnectedGenes() {
        return null;
    }

    /**
     *
     * @return a collection of isoforms, each supported by at least one gene
     */
    @Override
    public Collection<IEntity> GetConnectedIsoforms() {
        return null;
    }

    /**
     *
     * @return a collection of pairwise interactions, each supported by at least one isoform
     */
    @Override
    public Collection<IEntity> GetConnectedPairwiseInteractions() {
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
