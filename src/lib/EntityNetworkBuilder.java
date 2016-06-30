package lib;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by burkhart on 6/7/16.
 */
public class EntityNetworkBuilder implements IEntityNetworkBuilder {
    private static IEntityNetworkBuilder entityNetworkBuilder = null;
    private Collection<IEntity> variants;
    private Collection<IEntity> genes;
    private Collection<IEntity> isoforms;
    private Collection<IEntity> pairwiseInteractions;
    private ILogger logger;
    private IGenome referenceGenome;
    private boolean refreshRequired;

    public EntityNetworkBuilder(IGenome referenceGenome, ILogger logger){
        this.referenceGenome = referenceGenome;
        this.logger = logger;
        this.variants = new HashSet<>();
        this.genes = new HashSet<>();
        this.isoforms = new HashSet<>();
        this.pairwiseInteractions = new HashSet<>();
    }

    private void RefreshEntityNetwork() {
        if (this.refreshRequired) {
            /**
             * read through variants, creating and connecting genes
             * read through genes, creating and connecting isoforms
             * read through pairwise interactions, connecting existing isoforms
             * remove pairwise interactions not connected to isoforms
             * remove isoforms not connected to pairwise interactions
             * remove genes not connected to isoforms
             * remove remove variants not connected to genes
             */
        }
    }

    @Override
    public void AddDisconnectedVariants(IDataSourceReader dataSourceReader, IDataSource variantDataSource) {
        if(variantDataSource instanceof File) {
            this.variants.addAll(
                    dataSourceReader.Read(variantDataSource));
        }
        this.refreshRequired = true;
    }

    @Override
    public void AddDisconnectedPairwiseInteractions(IDataSourceReader dataSourceReader, IDataSource pairwiseInteractionDataSource) {
        if(pairwiseInteractionDataSource instanceof File) {
            this.pairwiseInteractions.addAll(
                    dataSourceReader.Read(pairwiseInteractionDataSource));
        }
        this.refreshRequired = true;
    }

    /**
     *
     * @return a collection of variants
     */
    @Override
    public Collection<IEntity> GetConnectedVariants() {
        RefreshEntityNetwork();
        return this.variants; }

    /**
     *
     * @return a collection of genes, each supported by at least one variant
     */
    @Override
    public Collection<IEntity> GetConnectedGenes() {
        RefreshEntityNetwork();
        return this.genes;
    }

    /**
     *
     * @return a collection of isoforms, each supported by at least one gene
     */
    @Override
    public Collection<IEntity> GetConnectedIsoforms() {
        RefreshEntityNetwork();
        return this.isoforms;
    }

    /**
     *
     * @return a collection of pairwise interactions, each supported by at least one isoform
     */
    @Override
    public Collection<IEntity> GetConnectedPairwiseInteractions() {
        RefreshEntityNetwork();
        return this.pairwiseInteractions;
    }
}
