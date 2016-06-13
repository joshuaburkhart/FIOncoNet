package lib;

/**
 * Created by burkhart on 6/12/16.
 */
public interface ITranscript {
    IIsoform PrincipleIsoform = null;
    java.util.Collection<IIsoform> AlternateIsoforms = null;
    java.util.List<INucleotide> Nucleotides = null;
}
