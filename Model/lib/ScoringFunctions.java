import java.util.function.Function;

/**
 * Created by burkhart on 6/16/16.
 */
public class ScoringFunctions {
    public static Function<IEntity, Double> ChildScoreSum = new Function<IEntity, Double>() {
        public Double apply(IEntity entity) {
            Double sum = 0.0;
            for (INode child : entity.GetChildren()) {
                IEntity childEntity = (IEntity) child;
                sum += childEntity.GetScore();
            }
            return sum;
        }
    };
    public static Function<IEntity, Double> CountChildren = new Function<IEntity, Double>() {
        public Double apply(IEntity entity) {
            Double count = 0.0;
            for (INode child : entity.GetChildren()) {
                count += child.GetChildren().size();
            }
            return count;
        }
    };

    public static Function<IEntity, Double> ONE = new Function<IEntity, Double>() {
        public Double apply(IEntity entity) {
            return 1.0;
        }
    };
}
