import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public interface INode {
    Collection<INode> GetParents();

    Collection<INode> GetChildren();

}
