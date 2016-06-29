package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/29/16.
 */
public interface IFileParser extends IDataSourceReader {
    Collection<IEntity> ParseEntitiesFromFile(IVariantFile variantFile);
}
