

import java.io.File;

/**
 * Created by burkhart on 9/30/16.
 */
public interface IDockingJob extends IEntity{
    IIsoform getIsoformR();

    IIsoform getIsoformL();

    String getShortName();

    File Subdirectory();

    void CreateOutputDirectory();
    String StructureNamePath();
    int getDockingRangeMax();
    int getDockingRangeMin();
    String getDockingFileExt();
    String CreateHexMacro();
    String CreateHexShellScript(String hexExePath, String macPath);
    void ExecuteShellScript(String shellPath);
}
