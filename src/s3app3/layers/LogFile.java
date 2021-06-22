package s3app3.layers;

import java.io.*;
import java.util.logging.*;


public class LogFile {
    private String fileName = "liaisonDeDonnes.log";
    private Logger logger;
    private static LogFile instance = null;

    public static LogFile getInstance()
    {
        if (instance == null)
            instance = new LogFile();

        return instance;
    }

    private LogFile() {


        logger = Logger.getLogger("MyLog");
        FileHandler fh;

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] %5$s %n");

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler(fileName);
            logger.setUseParentHandlers(false);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();

            LogRecord record = new LogRecord(Level.INFO, "%4$s: %5$s [%1$tc]%n");
            formatter.format(record);
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLog(String src, String message) {
        logger.info("["+ src+"] " +message);
    }
}