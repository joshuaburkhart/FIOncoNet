package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IVariantFile extends IVariantDataSource {
    Collection<String> GetLines();
}
