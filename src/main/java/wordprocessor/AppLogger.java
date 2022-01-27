package wordprocessor;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 *
 * @author andrewtaylor
 */
public class AppLogger {
    private static Logger logger;
    
    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("WordProcessor");
            logger.setLevel(Level.ALL);
            logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
            try {
                logger.addHandler(new FileHandler("WordProcessor.log", true));
                logger.info("Set up file logging");
            } catch (IOException e) {
                logger.warning(e.toString());
            }
        }
        return logger;
    }
}
