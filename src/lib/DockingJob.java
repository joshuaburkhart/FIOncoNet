package lib;

/**
 * Created by burkhart on 9/30/16.
 */
public class DockingJob implements IDockingJob{
    private IIsoform isoformR;
    private IIsoform isoformL;

    public DockingJob(IIsoform isoformR, IIsoform isoformL){
        this.isoformR = isoformR;
        this.isoformL = isoformL;
    }

    @Override
    public IIsoform getIsoformR() {
        return this.isoformR;
    }

    @Override
    public IIsoform getIsoformL() {
        return this.isoformL;
    }

    @Override
    public String toString(){
        return "isoformR: " +
                this.isoformR.GetGeneSymbol() + ", " +
                this.isoformR.GetPdbPath() + " -- " +
                "isoformL: " +
                this.isoformL.GetGeneSymbol() + ", " +
                this.isoformL.GetPdbPath();
    }

    @Override
    public String shortName(){
        return this.isoformR.GetGeneSymbol() + "-" + this.isoformL.GetGeneSymbol();
    }
}
