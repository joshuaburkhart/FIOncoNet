package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IIsoform extends IEntity {
    Collection<IDomain> GetDomains();
    String GetGeneSymbol();
    String GetPdbPath();
    void AddPairwiseInteraction(IPairwiseInteraction pairwiseInteraction);
    Collection<IPairwiseInteraction> GetPairwiseInteractions();
    IGene GetGene();
}
