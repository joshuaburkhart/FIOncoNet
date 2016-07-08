package lib;

import java.util.Collection;
import java.util.List;

/**
 * Created by burkhart on 6/16/16.
 */
public interface IReportGenerator {
    void WriteEntitiesToFileSystem(List<IEntity> entities);
}
