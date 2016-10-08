import java.util.*;

/**
 * Created by burkhart on 6/16/16.
 */
public class Filter implements IFilter {
    private ILogger logger;

    private Comparator<IEntity> scoreComparator = new Comparator<IEntity>() {
        public int compare(IEntity c1, IEntity c2) {
            return (int) Math.ceil(c1.GetScore() - c2.GetScore());
        }
    };

    public Filter(ILogger logger) {
        this.logger = logger;
    }

    @Override
    public List<IEntity> TopPercentile(Double percentile, Collection<IEntity> input) {
        List<IEntity> topPercentile = new ArrayList<>(input);
        List<IEntity> removeEntities = new ArrayList<>();
        int topCount = (int) Math.ceil(input.size() / percentile);
        Collections.sort(topPercentile, scoreComparator);
        for (int i = 0; i < topPercentile.size(); i++) {
            if (i >= topCount) {
                removeEntities.add(topPercentile.get(i));
            }
        }
        topPercentile.removeAll(removeEntities);
        return topPercentile;
    }

    @Override
    public List<IEntity> BottomPercentile(Double percentile, Collection<IEntity> input) {
        return null;
    }

    @Override
    public List<IEntity> TopNumber(int number, Collection<IEntity> input) {
        return null;
    }

    @Override
    public List<IEntity> BottomNumber(int number, Collection<IEntity> input) {
        return null;
    }

    @Override
    public List<IEntity> Zero(Collection<IEntity> input) {
        return null;
    }

    @Override
    public List<IEntity> NonZero(Collection<IEntity> input) {
        return null;
    }

    @Override
    public List<IEntity> AboveThreshold(Double threshold, Collection<IEntity> input) {
        return null;
    }

    @Override
    public List<IEntity> BelowThreshold(Double threshold, Collection<IEntity> input) {
        return null;
    }
}
