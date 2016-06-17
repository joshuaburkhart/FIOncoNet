package lib;

import java.util.Collection;
import java.util.function.Function;

/**
 * Created by burkhart on 6/16/16.
 */
public class Filter implements IFilter {
    @Override
    public Collection<IEntity> TopPercentile(Double percentile, Function<Double, INode> scoringFunction, Collection<IEntity> input) {
        return null;
    }

    @Override
    public Collection<IEntity> BottomPercentile(Double percentile, Function<Double, INode> scoringFunction, Collection<IEntity> input) {
        return null;
    }

    @Override
    public Collection<IEntity> TopNumber(int number, Function<Double, INode> scoringFunction, Collection<IEntity> input) {
        return null;
    }

    @Override
    public Collection<IEntity> BottomNumber(int number, Function<Double, INode> scoringFunction, Collection<IEntity> input) {
        return null;
    }

    @Override
    public Collection<IEntity> Zero(Function<Double, INode> scoringFunction, Collection<IEntity> input) {
        return null;
    }

    @Override
    public Collection<IEntity> NonZero(Function<Double, INode> scoringFunction, Collection<IEntity> input) {
        return null;
    }

    private static IFilter filter = null;

    public static IFilter Instance(){
        if(filter == null){
            filter = new Filter();
        }
        return filter;
    }
}
