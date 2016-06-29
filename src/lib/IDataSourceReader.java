package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/28/16.
 */
public interface IDataSourceReader {
    Collection<IEntity> Read(IDataSource dataSource);
}
