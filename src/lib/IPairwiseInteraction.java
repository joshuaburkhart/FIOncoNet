package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IPairwiseInteraction extends IEntity {
    String GetGeneSymbol1();
    String GetGeneSymbol2();
    Collection<IIsoform> GetIsoforms();
    void SetIsoforms(Collection<IIsoform> isoforms);
    void AddIsoform(IIsoform isoform);
    String GetPDB();
}
