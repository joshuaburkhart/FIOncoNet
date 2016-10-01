package lib;

/**
 * Created by burkhart on 9/30/16.
 */
public interface IDockingJob {
    IIsoform getIsoformR();
    IIsoform getIsoformL();
    String shortName();
}
