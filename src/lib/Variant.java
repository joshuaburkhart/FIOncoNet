package lib;

import java.util.HashSet;

/**
 * Created by burkhart on 6/7/16.
 */
public class Variant extends Entity implements IVariant{
    private String geneSymbol,ref,alt,qual,filter;
    private long startPos,endPos;
    private int NCBIBuild;
    private IGene gene;

    public Variant(String geneSymbol, long startPos, long endPos, int NCBIBuild, String ref, String alt){
        this.geneSymbol = geneSymbol;
        this.startPos = startPos;
        this.endPos = endPos;
        this.NCBIBuild = NCBIBuild;
        this.ref = ref;
        this.alt = alt;
        this.children = null;
    }

    @Override
    public String GetGeneSymbol() {
        return this.geneSymbol;
    }

    @Override
    public IGene GetGene() {
        return this.gene;
    }

    @Override
    public void SetGene(IGene gene) {
        if(gene != this.gene) {
            this.gene = gene;
            this.parents = new HashSet<>();
            this.parents.add(gene);
        }
    }
}
