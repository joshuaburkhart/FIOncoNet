package lib;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IGene {
    IIsoform PrincipleIsoform = null;
    java.util.Collection<IIsoform> AlternateIsoforms = null;
    java.util.Collection<IVariant> Variants = null;
    java.util.List<INucleotide> Nucleotides = null;
    IGeneIdentifier Identifier = null;
}
