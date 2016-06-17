package lib;

import java.util.Collection;

/**
 * Created by burkhart on 6/16/16.
 */
public interface IReportGenerator {
    void WriteEntitiesToFileSystem(Collection<IEntity> entities);
}
