import java.util.Collection;

/**
 * Created by burkhart on 6/29/16.
 */
public interface ITextFile extends IDataSource {
    Collection<String> GetLines();
}


