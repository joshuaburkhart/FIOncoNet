package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/29/16.
 */
public abstract class FileParser implements IDataSourceReader {
    public abstract Collection<IEntity> ParseEntitiesFromFile(IEntityFile entityFile);

    public Collection<IEntity> Read(IDataSource dataSource) {
        return ParseEntitiesFromFile((IEntityFile) dataSource);
    }
}
