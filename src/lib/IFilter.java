package lib;

import java.util.Collection;
import java.util.function.Function;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IFilter {
    Collection<IEntity> TopPercentile(Double percentile, Collection<IEntity> input);
    Collection<IEntity> BottomPercentile(Double percentile, Collection<IEntity> input);
    Collection<IEntity> TopNumber(int number, Collection<IEntity> input);
    Collection<IEntity> BottomNumber(int number, Collection<IEntity> input);
    Collection<IEntity> Zero(Collection<IEntity> input);
    Collection<IEntity> NonZero(Collection<IEntity> input);
    Collection<IEntity> AboveThreshold(Double threshold, Collection<IEntity> input);
    Collection<IEntity> BelowThreshold(Double threshold, Collection<IEntity> input);
}
