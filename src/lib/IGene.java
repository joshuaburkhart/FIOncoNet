package lib;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IGene {
    java.util.Collection<IVariant> Variants = null;
    IGeneIdentifier Identifier = null;
    java.util.Collection<ITranscript> AlternateTranscripts = null;
    ITranscript PrincipleTranscript = null;
}
