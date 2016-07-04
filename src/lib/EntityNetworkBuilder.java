package lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by burkhart on 6/7/16.
 */
public class EntityNetworkBuilder implements IEntityNetworkBuilder {
    private Collection<IEntity> variants;
    private Collection<IEntity> genes;
    private Collection<IEntity> isoforms;
    private Collection<IEntity> pairwiseInteractions;
    private ILogger logger;
    private boolean refreshRequired;

    public EntityNetworkBuilder(ILogger logger){
        this.logger = logger;
        this.variants = new HashSet<>();
        this.genes = new HashSet<>();
        this.isoforms = new HashSet<>();
        this.pairwiseInteractions = new HashSet<>();
    }

    private void RefreshEntityNetwork() {
        if (this.refreshRequired) {

            // read through variants, creating and connecting genes
            Map<String,Collection<IVariant>> geneSymbolVariantMap = new HashMap<>();
            for(IEntity entity : this.variants){
                IVariant variant = (IVariant) entity;
               String geneSymbol = variant.GetGeneSymbol();
                if(!geneSymbolVariantMap.containsKey(geneSymbol)){
                    geneSymbolVariantMap.put(geneSymbol,new HashSet<>());
                }
                geneSymbolVariantMap.get(geneSymbol).add(variant);
            }
            for(String geneSymbol : geneSymbolVariantMap.keySet()){
                IGene gene = new Gene(geneSymbol,geneSymbolVariantMap.get(geneSymbol));
                for(IVariant variant : gene.GetVariants()){
                    variant.SetGene(gene);
                }
                this.genes.add(gene);
            }

            // read through genes, creating and connecting isoforms
            Map<String,IIsoform> geneSymbolIsoformMap = new HashMap<>();
            for(IEntity entity : this.genes) {
                IGene gene = (IGene) entity;
                IIsoform isoform = new Isoform(gene);
                String geneSymbol = gene.GetGeneSymbol();
                geneSymbolIsoformMap.put(geneSymbol,isoform);
                gene.SetPrincipleIsoform(isoform);
                this.isoforms.add(isoform);
            }

            // read through pairwise interactions, connecting existing isoforms
            for(IEntity entity : this.pairwiseInteractions){
                IPairwiseInteraction pairwiseInteraction = (IPairwiseInteraction) entity;
                String geneSymbol1 = pairwiseInteraction.GetGeneSymbol1();
                String geneSymbol2 = pairwiseInteraction.GetGeneSymbol2();
                if(geneSymbolIsoformMap.containsKey(geneSymbol1)){
                    IIsoform isoform1 = geneSymbolIsoformMap.get(geneSymbol1);
                    pairwiseInteraction.AddIsoform(isoform1);
                    isoform1.AddPairwiseInteraction(pairwiseInteraction);
                }
                if(geneSymbolIsoformMap.containsKey(geneSymbol2)){
                    IIsoform isoform2 = geneSymbolIsoformMap.get(geneSymbol2);
                    pairwiseInteraction.AddIsoform(isoform2);
                    isoform2.AddPairwiseInteraction(pairwiseInteraction);
                }
            }

            // remove pairwise interactions not connected to isoforms
            Collection<IEntity> removePairwiseInteractions = new HashSet<>();
            for(IEntity entity : this.pairwiseInteractions){
                IPairwiseInteraction pairwiseInteraction = (IPairwiseInteraction) entity;
                if(pairwiseInteraction.GetIsoforms().isEmpty()){
                    removePairwiseInteractions.add(entity);
                }
            }
            for(IEntity entity : removePairwiseInteractions){
                this.pairwiseInteractions.remove(entity);
            }

            // remove isoforms not connected to pairwise interactions and null referring gene's principle isoform
            Collection<IEntity> removeIsoforms = new HashSet<>();
            for(IEntity entity : this.isoforms){
                IIsoform isoform = (IIsoform) entity;
                if(isoform.GetPairwiseInteractions().isEmpty()){
                    isoform.GetGene().SetPrincipleIsoform(null);
                    removeIsoforms.add(entity);
                }
            }
            for(IEntity entity : removeIsoforms){
                this.isoforms.remove(entity);
            }

            // remove genes not connected to isoforms and null referring variants' gene
            Collection<IEntity> removeGenes = new HashSet<>();
            for(IEntity entity : this.genes){
                IGene gene = (IGene) entity;
                if(gene.GetPrincipleIsoform() == null){
                    for(IVariant variant : gene.GetVariants()){
                        variant.SetGene(null);
                    }
                    removeGenes.add(entity);
                }
            }
            for(IEntity entity : removeGenes){
                this.genes.remove(entity);
            }

            // remove remove variants not connected to genes
            Collection<IEntity> removeVariants = new HashSet<>();
            for(IEntity entity : this.variants){
                IVariant variant = (IVariant) entity;
                if(variant.GetGene() == null){
                    removeVariants.add(entity);
                }
            }
            for(IEntity entity : removeVariants){
                this.variants.remove(entity);
            }

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
