package lib;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by burkhart on 6/7/16.
 */
public class Gene extends Entity implements IGene {
    private String geneSymbol;
    private String uniprot;
    private IIsoform principleIsoform;
    private Collection<IVariant> variants;

    public Gene(String geneSymbol){
        this(geneSymbol,null,null);
    }

    public Gene(String geneSymbol, String uniprot){
        this(geneSymbol,null,uniprot);
    }

    public Gene(String geneSymbol, Collection<IVariant> variants){
        this(geneSymbol,variants,null);
    }

    public Gene(String geneSymbol, Collection<IVariant> variants, String uniprot){
        this.geneSymbol = geneSymbol;
        this.uniprot = uniprot;
        this.variants = variants;
        this.children = new HashSet<>();
        this.children.addAll(variants);
    }

    @Override
    public String GetGeneSymbol() {
        return this.geneSymbol;
    }

    @Override
    public String GetUniprot() {
        return this.uniprot;
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

    @Override
    public String ToString() {
       String ret = this.geneSymbol;
        for(IVariant variant : this.variants){
            ret += "\n\t\t\t" + variant.ToString();
        }
        return ret;
    }
}
