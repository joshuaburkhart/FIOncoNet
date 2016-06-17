package lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

/**
 * Created by burkhart on 6/12/16.
 */
public abstract class Entity implements IEntity {
    protected Double score = -1.0;
    private Function<IEntity,Double> lastScoringFunction = null;

    public void ApplyScoringFunction(Function<IEntity,Double> scoringFunction){
        if(scoringFunction != lastScoringFunction) {
            score = scoringFunction.apply(this);
        }
    }

    public Double GetScore(){
        return score;
    }

    public Collection<INode> GetChildren(){
       return null;
    }

    public Collection<INode> GetParents(){
        return null;
    }
}
