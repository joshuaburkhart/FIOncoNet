package lib;

import java.util.Collection;
import java.util.Map;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IVariantFileParser extends IDataSourceReader {
    Collection<IEntity> ParseVariantsFromFile(IVariantFile variantFile);
}
