package lib;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by burkhart on 6/7/16.
 */
public class VCFFileParser extends FileParser {

    private static String VCF_VARIANT_PARAMETER_CAPTURE = "^(?<chrom>[^#\\s]+)\\s+(?<pos>[^\\s]+)\\s+(?<id>[^\\s]+)\\s+(?<ref>[^\\s]+)\\s+(?<alt>[^\\s]+)\\s+(?<qual>[^\\s]+)\\s+(?<filter>[^\\s]+).*";
    private static String VCF_ALT_CAPTURE = "([^,\\s]+)";
    private static String CHROM = "chrom";
    private static String POS = "pos";
    private static String ID = "id";
    private static String REF = "ref";
    private static String ALT = "alt";
    private static String QUAL = "qual";
    private static String FILTER = "filter";
    private Pattern variantParameterPattern;
    private Pattern altPattern;
    private IGenome referenceGenome;
    private ILogger logger;

    public VCFFileParser(IGenome referenceGenome, ILogger logger){
        this.logger = logger;
        this.variantParameterPattern = Pattern.compile(this.VCF_VARIANT_PARAMETER_CAPTURE);
        this.altPattern = Pattern.compile(this.VCF_ALT_CAPTURE);
        this.referenceGenome = referenceGenome;
    }

    @Override
    public Collection<IEntity> ParseEntitiesFromFile(IFile variantFile) {
        if(ReferenceGenomeMatchesVariantFile()) {
            Collection<IEntity> variants = new HashSet<>();

            for (String line : variantFile.GetLines()) {
                Matcher matcher = this.variantParameterPattern.matcher(line);

                if (matcher.find()) {
                    long startPos = Long.parseLong(matcher.group(this.POS));
                    long endPos = startPos + (long) matcher.group(this.REF).length();
                    Collection<String> geneSymbols = referenceGenome.GetGeneSymbolsForLocus(matcher.group(this.CHROM), startPos, endPos);
                    Collection<String> alts = ParseAltsFromVCFAltGroup(this.ALT);
                    for (String geneSymbol : geneSymbols) {
                        for (String alt : alts) {
                            variants.add(new Variant(geneSymbol,
                                    startPos,
                                    endPos,
                                    referenceGenome.GetNCBIBuild(),
                                    matcher.group(this.REF),
                                    matcher.group(alt)));
                        }
                    }
                }
            }

            return variants;
        }else{
            String msg = "Variant file supplied does not match reference genome in parser.";
            this.logger.Log(LoggingLevel.ERROR,msg);
            throw new IllegalArgumentException(msg);
        }
    }

    private Collection<String> ParseAltsFromVCFAltGroup(String altGroup){
        Collection<String> alts = new HashSet<>();
        Matcher matcher = this.altPattern.matcher(altGroup);
        while (matcher.find()) {
            alts.add(matcher.group(1));
        }
        return alts;
    }

    private boolean ReferenceGenomeMatchesVariantFile(){
        throw new NotImplementedException();
    }
}
