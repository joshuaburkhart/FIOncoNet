import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.io.*;
import java.util.*;

import static org.apache.poi.ss.usermodel.WorkbookFactory.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by burkhart on 10/8/16.
 */
public abstract class XlsxFile implements IXlsxFile {
    final String filePath;
    final String sheetName;
    ILogger logger;

    public XlsxFile(String filePath, String sheetName, ILogger logger) {
        this.filePath = filePath;
        this.sheetName = sheetName;
        this.logger = logger;
    }

    public Collection<Row> GetRows() {
        Collection<Row> rows = new ArrayList<>();
        try {
            InputStream inp = new FileInputStream(this.filePath);

            Workbook wb = create(inp);
            Sheet sheet = wb.getSheet(this.sheetName);
            Iterator rit = sheet.rowIterator();
            while (rit.hasNext()) {
                rows.add((Row) rit.next());
            }
        }catch (FileNotFoundException fnfe) {
            logger.Log(LoggingLevel.ERROR, "could not find file '" + this.filePath + "'", fnfe);
        } catch (IOException ioe) {
            logger.Log(LoggingLevel.ERROR, "io exception", ioe);
        }catch (InvalidFormatException ife){
            logger.Log(LoggingLevel.ERROR, "invalid format exception",ife);
        }
        return rows;
    }
}
