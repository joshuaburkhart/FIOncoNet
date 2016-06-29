package lib;

/**
 * Created by burkhart on 6/29/16.
 */
public class ReactomeFIFileParser implements IFileParser {
    private static String REACTOME_FI_PAIRWISE_INTERACTION_CAPTURE = "^(?<gene1>[^\\t]+)\\t+(?<gene2>[^\\t]+)\\t+(?<annotation>[^\\t]+)\\t+(?<direction>[^\\t]+)\\t+(?<score>[0-9.]+).*";
}
