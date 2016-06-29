package lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by burkhart on 6/7/16.
 */
public class VCFFileParser implements IVariantFileParser {

    private static String VCF_VARIANT_PARAMETER_CAPTURE = "^(?<chrom>[^#\\s]+)\\s+(?<pos>[^\\s]+)\\s+(?<id>[^\\s]+)\\s+(?<ref>[^\\s]+)\\s+(?<alt>[^\\s]+)\\s+(?<qual>[^\\s]+)\\s+(?<filter>[^\\s]+).*";
    private static String CHROM = "chrom";
    private static String POS = "pos";
    private static String ID = "id";
    private static String REF = "ref";
    private static String ALT = "alt";
    private static String QUAL = "qual";
    private static String FILTER = "filter";
    private Pattern pattern;

    public VCFFileParser(){
        this.pattern = Pattern.compile(this.VCF_VARIANT_PARAMETER_CAPTURE);
    }

    @Override
    public Collection<IEntity> ParseVariantsFromFile(IVariantFile variantFile) {
        Collection<IEntity> variants = new HashSet<>();

        for(String line : variantFile.GetLines()){
            Matcher matcher = this.pattern.matcher(line);

            if(matcher.find()) {
               variants.add(new Variant(matcher.group(this.CHROM),
                            matcher.group(this.POS),
                            matcher.group(this.ID),
                            matcher.group(this.REF),
                            matcher.group(this.ALT),
                            matcher.group(this.QUAL),
                            matcher.group(this.FILTER)));
            }
        }

        return variants;
    }

    @Override
    public Collection<IEntity> Read(IDataSource dataSource) {
        return ParseVariantsFromFile((IVariantFile) dataSource);
    }
}
