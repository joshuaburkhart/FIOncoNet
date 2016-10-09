import java.util.Collection;

/**
 * Created by burkhart on 10/8/16.
 */
public abstract class XlsxFileParser implements IDataSourceReader {
    public abstract Collection<IEntity> ParseEntitiesFromFile(IXlsxFile entityFile);

    public Collection<IEntity> Read(IDataSource dataSource) {
        return ParseEntitiesFromFile((IXlsxFile) dataSource);
    }
}
