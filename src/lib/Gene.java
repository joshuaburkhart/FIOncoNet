package lib;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by burkhart on 6/7/16.
 */
public class Gene extends Entity implements IGene {
    private String geneSymbol;
    private IIsoform principleIsoform;
    private Collection<IVariant> variants;

    public Gene(String geneSymbol){
        this.geneSymbol = geneSymbol;
    }

    public Gene(String geneSymbol, Collection<IVariant> variants){
        this.geneSymbol = geneSymbol;
        this.variants = variants;
        this.children = new HashSet<>();
        this.children.addAll(variants);
    }

    @Override
    public String GetGeneSymbol() {
        return this.geneSymbol;
    }

    @Override
    public void SetVariants(Collection<IVariant> variants) {
        this.variants = variants;
        this.children = new HashSet<>();
        this.children.addAll(variants);
    }

    @Override
    public Collection<IVariant> GetVariants() {
        return this.variants;
    }

    @Override
    public IIsoform GetPrincipleIsoform() {
        return this.principleIsoform;
    }

    @Override
    public void SetPrincipleIsoform(IIsoform isoform) {
       if(isoform != this.principleIsoform) {
           this.principleIsoform = isoform;
           this.parents = new HashSet<>();
           this.parents.add(isoform);
       }
    }
}
