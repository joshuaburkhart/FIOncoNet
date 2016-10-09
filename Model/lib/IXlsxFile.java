import org.apache.poi.ss.usermodel.Row;

import java.util.Collection;

/**
 * Created by burkhart on 10/8/16.
 */
public interface IXlsxFile extends IDataSource {
    Collection<Row> GetRows();
}
