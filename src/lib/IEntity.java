package lib;

import java.util.function.Function;

/**
 * Created by burkhart on 6/12/16.
 */
public interface IEntity extends INode{
    void ApplyScoringFunction(Function<IEntity,Double> scoringFunction);
    Double GetScore();

}
