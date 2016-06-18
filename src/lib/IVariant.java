package lib;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IVariant extends IEntity {
    java.util.List<INucleotide> ReferenceNucleotides = null;
    java.util.List<INucleotide> AlternateNucleotides = null;
    IGeneIdentifier GeneIdentifier = null;
}