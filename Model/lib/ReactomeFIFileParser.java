import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by burkhart on 6/29/16.
 */
public class ReactomeFIFileParser extends TextFileParser {
    private static String REACTOME_FI_PAIRWISE_INTERACTION_CAPTURE = "^(?<gene1>[^\\t]+)\\t+(?<gene2>[^\\t]+)\\t+(?<annotation>[^\\t]+)\\t+(?<direction>[^\\t]+)\\t+(?<score>[0-9.]+).*";
    private static String GENE1 = "gene1";
    private static String GENE2 = "gene2";
    private static String ANNOTATION = "annotation";
    private static String DIRECTION = "direction";
    private static String SCORE = "score";
    private Pattern pattern;
    private ILogger logger;

    public ReactomeFIFileParser(ILogger logger) {
        this.logger = logger;
        this.pattern = Pattern.compile(this.REACTOME_FI_PAIRWISE_INTERACTION_CAPTURE);
    }

    @Override
    public Collection<IEntity> ParseEntitiesFromFile(ITextFile pairwiseInteractionFile) {
        Collection<IEntity> pairwiseInteractions = new HashSet<>();

        for (String line : pairwiseInteractionFile.GetLines()) {
            Matcher matcher = this.pattern.matcher(line);

            if (matcher.find()) {
                pairwiseInteractions.add(new PairwiseInteraction(matcher.group(this.GENE1),
                        matcher.group(this.GENE2),
                        matcher.group(this.ANNOTATION),
                        matcher.group(this.DIRECTION),
                        matcher.group(this.SCORE)));
            }
        }

        return pairwiseInteractions;
    }
}
