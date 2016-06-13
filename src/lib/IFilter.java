package lib;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IFilter {
    java.util.Collection<IEntity> InputEntities = null;
    java.util.Collection<IEntity> OutputEntities = null;
    IScoringFunction ScoringFunction = null;
}
