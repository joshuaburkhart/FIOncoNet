import java.util.Collection;
import java.util.List;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IFilter {
    List<IEntity> TopPercentile(Double percentile, Collection<IEntity> input);

    List<IEntity> BottomPercentile(Double percentile, Collection<IEntity> input);

    List<IEntity> TopNumber(int number, Collection<IEntity> input);

    List<IEntity> BottomNumber(int number, Collection<IEntity> input);

    List<IEntity> Zero(Collection<IEntity> input);

    List<IEntity> NonZero(Collection<IEntity> input);

    List<IEntity> AboveThreshold(Double threshold, Collection<IEntity> input);

    List<IEntity> BelowThreshold(Double threshold, Collection<IEntity> input);
}
