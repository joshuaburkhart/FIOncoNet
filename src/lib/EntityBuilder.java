package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public class EntityBuilder implements IEntityBuilder {
    IVariantFileParser variantFileParser;

    @Override
    public void AddVariants(IVariantDataSource variantDataSource) {
        if(variantDataSource instanceof IVariantFile) {
            variantFileParser.ParseVariantsFromFile((IVariantFile) variantDataSource);
        }
    }

    @Override
    public void AddPairwiseInteractions(IPairwiseInteractionDataSource pairwiseInteractionDataSource) {

    }

    /**
     *
     * @return a collection of variants
     */
    @Override
    public Collection<IEntity> GetVariants() { return null; }

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
