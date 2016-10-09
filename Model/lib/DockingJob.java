
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by burkhart on 9/30/16.
 */
public class DockingJob extends Entity implements IDockingJob {
    private ILogger logger;
    private IIsoform isoformR;
    private IIsoform isoformL;
    private String parentDirectory;
    private String shortName;
    private File subdirectory;

    public DockingJob(IIsoform isoformR, IIsoform isoformL,ILogger logger,String parentDirectory) {
        this.logger = logger;
        this.isoformR = isoformR;
        this.isoformL = isoformL;
        this.parentDirectory = parentDirectory;
        this.shortName = null;
        this.subdirectory = null;
    }

    @Override
    public IIsoform getIsoformR() {
        return this.isoformR;
    }

    @Override
    public IIsoform getIsoformL() {
        return this.isoformL;
    }

    @Override
    public String toString() {
        return "isoformR: " +
                this.isoformR.GetGeneSymbol() + ", " +
                this.isoformR.GetPdbPath() + " -- " +
                "isoformL: " +
                this.isoformL.GetGeneSymbol() + ", " +
                this.isoformL.GetPdbPath();
    }

    @Override
    public String getShortName() {
        if(this.shortName == null) {
            this.shortName = this.isoformR.GetGeneSymbol() + "-" + this.isoformL.GetGeneSymbol();
        }
        return this.shortName;
    }

    @Override
    public String ToString() {
        return null;
    }

    @Override
    public File Subdirectory(){
        if(this.subdirectory == null) {
            this.subdirectory = new java.io.File(this.parentDirectory + "/" + this.getShortName());
        }
        return this.subdirectory;
    }

    @Override
    public void CreateOutputDirectory(){
        java.io.File dockingDirectory = this.Subdirectory();

        // if the directory does not exist, create it
        if (!dockingDirectory.exists()) {
            System.out.println("creating directory: " + dockingDirectory);
            boolean result = false;

            try{
                dockingDirectory.mkdir();
                result = true;
            }
            catch(SecurityException se){
                this.logger.Log(LoggingLevel.ERROR,"security exception thrown while creating docking directory",se);
            }
            if(result) {
                this.logger.Log(LoggingLevel.INFO,"created output directory '" + dockingDirectory.getPath() + "'");
            }
        }
    }

    @Override
    public String StructurePath() {
        return this.Subdirectory().getPath() + "/" + this.getShortName() + ".pdb.gz";
    }

    @Override
    public String CreateHexMacro() {
        String macText =
                "open_receptor " + this.isoformR.GetPdbPath() + "\n" +
                "open_ligand " + this.isoformL.GetPdbPath() + "\n" +
                "activate_docking\n" +
                "save_both " + this.StructurePath() + "\n";

        //log .mac file
        this.logger.Log(LoggingLevel.INFO,"Writing Macro:");
        this.logger.Log(LoggingLevel.INFO,macText);

        //write .mac file to output directory
        String macPath = this.Subdirectory().getPath() + "/hex_macro.mac";
        try(  PrintWriter out = new PrintWriter(macPath)  ){
            out.println(macText);
        }catch(FileNotFoundException fnf){
            this.logger.Log(LoggingLevel.FATAL,"can't write macro file",fnf);
        }
        return macPath;
    }

    @Override
    public String CreateHexShellScript(String hexExePath, String macPath) {
        //set hex log path
        String hexLogPath = this.Subdirectory().getPath() + "/hex_log.txt";
        //create shell command
        String shellText = hexExePath + " -ncpu 4 <" + macPath + " >" + hexLogPath;
        //log shell command
        this.logger.Log(LoggingLevel.INFO,"Writing Shell Command: " + shellText);
        String shellPath = this.Subdirectory().getPath() + "/hex_shell.sh";
        //write shell file to output directory
        try(  PrintWriter out = new PrintWriter(shellPath)  ){
            out.println(shellText);
        }catch(FileNotFoundException fnf){
            this.logger.Log(LoggingLevel.FATAL,"can't write shell file",fnf);
        }
        return shellPath;
    }

    @Override
    public void ExecuteShellScript(String shellPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(shellPath);
            Process p = pb.start();     // Start the process.
            p.waitFor();                // Wait for the process to finish.
            this.logger.Log(LoggingLevel.INFO,"docking complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
