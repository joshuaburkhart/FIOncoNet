package test;

import junit.framework.TestCase;
import ILogger;
import Logger;
import LoggingLevel;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by burkhart on 8/10/16.
 */
public class LoggerTest extends TestCase {
    private ILogger logger;
    private static String TEST_LOG_FN = "src/test/log/LoggerTest.log.txt";

    protected void setUp(){
        this.logger = new Logger(TEST_LOG_FN, LoggingLevel.INFO);
    }

    protected void tearDown(){
        this.logger.StopLogging();
        this.logger = null;
        new File(TEST_LOG_FN).delete();
    }

    @Test
    public void testLoggerNotNull(){
        Assert.assertNotNull("this.logger is null!", this.logger);
    }

    @Test
    public void testSetLoggingLevel() {
        Assert.assertEquals(this.logger.SetLoggingLevel(LoggingLevel.INFO), LoggingLevel.INFO);
        Assert.assertEquals(this.logger.SetLoggingLevel(LoggingLevel.ERROR), LoggingLevel.ERROR);
        Assert.assertEquals(this.logger.SetLoggingLevel(LoggingLevel.FATAL), LoggingLevel.FATAL);
    }

    @Test
    public void testStringTimeStamp() throws Exception {
        String tsRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}$";
        String ts = this.logger.StringTimeStamp();
        Assert.assertTrue("Expected '"+ts+"' to match '"+tsRgx+"'",ts.matches(tsRgx));
    }

    @Test
    public void testLongTimeStamp() throws Exception {
        long ts = this.logger.LongTimeStamp();
        Assert.assertTrue(ts > 0);
    }

    @Test
    public void testLog() throws Exception {
        String iRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\t[0-9]+\\tINFO\\ttest INFO$";
        String wRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\t[0-9]+\\tWARNING\\ttest WARNING$";
        String eRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\t[0-9]+\\tERROR\\ttest ERROR$";
        String fRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\t[0-9]+\\tFATAL\\ttest FATAL$";
        String lTxt = null;

        // INFO
        this.logger.SetLoggingLevel(LoggingLevel.INFO);
        lTxt = this.logger.Log(LoggingLevel.INFO,"test INFO");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+iRgx+"'",lTxt.matches(iRgx));
        lTxt = this.logger.Log(LoggingLevel.WARNING,"test WARNING");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+wRgx+"'",lTxt.matches(wRgx));
        lTxt = this.logger.Log(LoggingLevel.ERROR,"test ERROR");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+eRgx+"'",lTxt.matches(eRgx));
        lTxt = this.logger.Log(LoggingLevel.FATAL,"test FATAL");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+fRgx+"'",lTxt.matches(fRgx));

        // WARNING
        this.logger.SetLoggingLevel(LoggingLevel.WARNING);
        lTxt = this.logger.Log(LoggingLevel.INFO,"test INFO");
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.WARNING,"test WARNING");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+wRgx+"'",lTxt.matches(wRgx));
        lTxt = this.logger.Log(LoggingLevel.ERROR,"test ERROR");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+eRgx+"'",lTxt.matches(eRgx));
        lTxt = this.logger.Log(LoggingLevel.FATAL,"test FATAL");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+fRgx+"'",lTxt.matches(fRgx));

        // ERROR
        this.logger.SetLoggingLevel(LoggingLevel.ERROR);
        lTxt = this.logger.Log(LoggingLevel.INFO,"test INFO");
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.WARNING,"test WARNING");
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.ERROR,"test ERROR");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+eRgx+"'",lTxt.matches(eRgx));
        lTxt = this.logger.Log(LoggingLevel.FATAL,"test FATAL");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+fRgx+"'",lTxt.matches(fRgx));

        // FATAL
        this.logger.SetLoggingLevel(LoggingLevel.FATAL);
        lTxt = this.logger.Log(LoggingLevel.INFO,"test INFO");
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.WARNING,"test WARNING");
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.ERROR,"test ERROR");
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.FATAL,"test FATAL");
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+fRgx+"'",lTxt.matches(fRgx));
    }

    @Test
    public void testLog1() throws Exception {
        String iRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\t[0-9]+\\tINFO\\ttest INFO\ttest Exception\ttest Exception$";
        String wRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\t[0-9]+\\tWARNING\\ttest WARNING\ttest Exception\ttest Exception$";
        String eRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\t[0-9]+\\tERROR\\ttest ERROR\ttest Exception\ttest Exception$";
        String fRgx = "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\t[0-9]+\\tFATAL\\ttest FATAL\ttest Exception\ttest Exception$";
        String lTxt = null;

        // INFO
        this.logger.SetLoggingLevel(LoggingLevel.INFO);
        lTxt = this.logger.Log(LoggingLevel.INFO,"test INFO",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+iRgx+"'",lTxt.matches(iRgx));
        lTxt = this.logger.Log(LoggingLevel.WARNING,"test WARNING",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+wRgx+"'",lTxt.matches(wRgx));
        lTxt = this.logger.Log(LoggingLevel.ERROR,"test ERROR",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+eRgx+"'",lTxt.matches(eRgx));
        lTxt = this.logger.Log(LoggingLevel.FATAL,"test FATAL",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+fRgx+"'",lTxt.matches(fRgx));

        // WARNING
        this.logger.SetLoggingLevel(LoggingLevel.WARNING);
        lTxt = this.logger.Log(LoggingLevel.INFO,"test INFO",new Exception("test Exception"));
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.WARNING,"test WARNING",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+wRgx+"'",lTxt.matches(wRgx));
        lTxt = this.logger.Log(LoggingLevel.ERROR,"test ERROR",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+eRgx+"'",lTxt.matches(eRgx));
        lTxt = this.logger.Log(LoggingLevel.FATAL,"test FATAL",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+fRgx+"'",lTxt.matches(fRgx));

        // ERROR
        this.logger.SetLoggingLevel(LoggingLevel.ERROR);
        lTxt = this.logger.Log(LoggingLevel.INFO,"test INFO",new Exception("test Exception"));
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.WARNING,"test WARNING",new Exception("test Exception"));
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.ERROR,"test ERROR",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+eRgx+"'",lTxt.matches(eRgx));
        lTxt = this.logger.Log(LoggingLevel.FATAL,"test FATAL",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+fRgx+"'",lTxt.matches(fRgx));

        // FATAL
        this.logger.SetLoggingLevel(LoggingLevel.FATAL);
        lTxt = this.logger.Log(LoggingLevel.INFO,"test INFO",new Exception("test Exception"));
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.WARNING,"test WARNING",new Exception("test Exception"));
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.ERROR,"test ERROR",new Exception("test Exception"));
        Assert.assertNull(lTxt);
        lTxt = this.logger.Log(LoggingLevel.FATAL,"test FATAL",new Exception("test Exception"));
        Assert.assertNotNull(lTxt);
        Assert.assertTrue("Expected '"+lTxt+"' to match '"+fRgx+"'",lTxt.matches(fRgx));
    }

    @Test(timeout = 100)//test fails after 100ms
    public void testStopLogging() throws Exception {
        this.logger.StopLogging();
    }

}