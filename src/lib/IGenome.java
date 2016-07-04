package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/29/16.
 */
public interface IGenome {
    Collection<String> GetGeneSymbolsForLocus(String chrom, long startPos, long endPos);
    int GetNCBIBuild();
}
