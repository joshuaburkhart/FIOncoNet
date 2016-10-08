import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by burkhart on 7/3/16.
 */
public class MAFFileParser extends FileParser {
    private static String MAF_VARIANT_PARAMETER_CAPTURE = "^(?<geneSymbol>[^\\s]+)\\s+[^\\s]+\\s+[^\\s]+\\s+(?<NCBIBuild>[^\\s]+)\\s+(?<chrom>[^\\s]+)\\s+(?<startPos>[0-9]+)\\s+(?<endPos>[0-9]+)\\s+[^\\s]+\\s+[^\\s]+\\s+[^\\s]+\\s+(?<ref>[^\\s]+)\\s+(?<altAllele1>[^\\s]+)\\s+(?<altAllele2>[^\\s]+).*";
    private static String GENE_SYMBOL = "geneSymbol";
    private static String NCBI_BUILD = "NCBIBuild";
    private static String CHROM = "chrom";
    private static String START_POS = "startPos";
    private static String END_POS = "endPos";
    private static String REF = "ref";
    private static String ALT_ALLELE_1 = "altAllele1";
    private static String ALT_ALLELE_2 = "altAllele2";
    private Pattern variantParameterPattern;
    private ILogger logger;

    public MAFFileParser(ILogger logger) {
        this.logger = logger;
        this.variantParameterPattern = Pattern.compile(this.MAF_VARIANT_PARAMETER_CAPTURE);
    }

    @Override
    public Collection<IEntity> ParseEntitiesFromFile(IFile variantFile) {
        Collection<IEntity> variants = new HashSet<>();

        for (String line : variantFile.GetLines()) {
            Matcher matcher = this.variantParameterPattern.matcher(line);

            if (matcher.find()) {
                Collection<String> alts = new HashSet<>();
                String ref = matcher.group(this.REF);
                String alt1 = matcher.group(this.ALT_ALLELE_1);
                String alt2 = matcher.group(this.ALT_ALLELE_2);

                if (!alt1.equals(ref)) {
                    alts.add(alt1);
                }

                if (!alt2.equals(ref)) {
                    alts.add(alt2);
                }

                for (String alt : alts) {
                    variants.add(new Variant(matcher.group(this.GENE_SYMBOL),
                            Long.parseLong(matcher.group(this.START_POS)),
                            Long.parseLong(matcher.group(this.END_POS)),
                            Integer.parseInt(matcher.group(this.NCBI_BUILD)),
                            matcher.group(this.REF),
                            alt));
                }
            }
        }

        return variants;
    }
}
