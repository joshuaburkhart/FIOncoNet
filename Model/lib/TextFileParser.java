import java.util.Collection;

/**
 * Created by burkhart on 6/29/16.
 */
public abstract class TextFileParser implements IDataSourceReader {
    public abstract Collection<IEntity> ParseEntitiesFromFile(ITextFile entityFile);

    public Collection<IEntity> Read(IDataSource dataSource) {
        return ParseEntitiesFromFile((ITextFile) dataSource);
    }
}
