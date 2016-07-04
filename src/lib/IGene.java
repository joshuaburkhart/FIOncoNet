package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IGene extends IEntity {
    String GetGeneSymbol();
    void SetVariants(Collection<IVariant> variants);
    Collection<IVariant> GetVariants();
    IIsoform GetPrincipleIsoform();
    void SetPrincipleIsoform(IIsoform isoform);
}
