import java.util.Collection;
import java.util.HashSet;

/**
 * Created by burkhart on 6/7/16.
 */
public class PairwiseInteraction extends Entity implements IPairwiseInteraction {
    private String gene1, gene2, annotation, direction, score;
    private Collection<IIsoform> isoforms;
    private String pdb;

    public PairwiseInteraction(String gene1, String gene2, String annotation, String direction, String score) {
        this.gene1 = gene1;
        this.gene2 = gene2;
        this.annotation = annotation;
        this.direction = direction;
        this.score = score;
        this.isoforms = new HashSet<>();
        this.children = new HashSet<>();
    }

    @Override
    public String GetGeneSymbol1() {
        return this.gene1;
    }

    @Override
    public String GetGeneSymbol2() {
        return this.gene2;
    }

    @Override
    public Collection<IIsoform> GetIsoforms() {
        return this.isoforms;
    }

    @Override
    public void SetIsoforms(Collection<IIsoform> isoforms) {
        this.isoforms = isoforms;
        this.children = new HashSet<>();
        this.children.addAll(isoforms);
    }

    @Override
    public void AddIsoform(IIsoform isoform) {
        this.isoforms.add(isoform);
        this.children.add(isoform);
    }

    @Override
    public String GetPDB() {
        return null;
    }

    @Override
    public String ToString() {
        String ret = this.gene1 + " " + this.direction + " " + this.gene2 + ":" + this.annotation;
        for (IIsoform isoform : this.isoforms) {
            ret += "\n\t" + isoform.ToString();
        }
        return ret;
    }
}
