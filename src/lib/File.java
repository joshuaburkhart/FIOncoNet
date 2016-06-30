package lib;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by burkhart on 6/29/16.
 */
public abstract class File implements IFile {
    final String filePath;
    ILogger logger;

    public File(String filePath, ILogger logger){
        this.filePath = filePath;
        this.logger = logger;
    }

    public Collection<String> GetLines(){
        Collection<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.filePath));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                lines.add(line);
            }
        }catch(FileNotFoundException fnfe){
            logger.Log(LoggingLevel.ERROR,"could not find file '"+this.filePath+"'",fnfe);
        } catch (IOException ioe) {
            logger.Log(LoggingLevel.ERROR,"io exception",ioe);
        }
        return lines;
    }
}
