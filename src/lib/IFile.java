package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/29/16.
 */
public interface IFile extends IDataSource {
    Collection<String> GetLines();
}
