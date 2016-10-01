package lib;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by burkhart on 6/7/16.
 */
public class Isoform extends Entity implements IIsoform {
    private IGene gene;
    private String pdbPath;
    private Collection<IPairwiseInteraction> pairwiseInteractions;

    public Isoform(IGene gene){
        this(gene,null);
    }

    public Isoform(IGene gene, String pdbPath){
        this.gene = gene;
        this.pairwiseInteractions = new HashSet<>();
        this.parents = new HashSet<>();
        this.children = new HashSet<>();
        this.children.add(gene);
        this.pdbPath = pdbPath;
    }

    @Override
    public Collection<IDomain> GetDomains() {
        return null;
    }

    @Override
    public String GetGeneSymbol() {
        return this.gene.GetGeneSymbol();
    }

    @Override
    public String GetPdbPath() {
        return this.pdbPath;
    }

    @Override
    public void AddPairwiseInteraction(IPairwiseInteraction pairwiseInteraction) {
        this.pairwiseInteractions.add(pairwiseInteraction);
        this.parents.add(pairwiseInteraction);
    }

    @Override
    public Collection<IPairwiseInteraction> GetPairwiseInteractions() {
        return this.pairwiseInteractions;
    }

    @Override
    public IGene GetGene() {
        return this.gene;
    }

    @Override
    public String ToString() {
        return this.gene.GetGeneSymbol() + "\n\t\t" + this.gene.ToString();
    }
}
