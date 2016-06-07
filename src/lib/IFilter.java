package lib;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IFilter {
    java.util.Collection<INode> InputNodes = null;
    java.util.Collection<INode> OutputNodes = null;
    IScoringFunction ScoringFunction = null;
}
