package wordprocessor;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author andrewtaylor
 */
public class Config {
    private Properties properties;
    private Logger logger;
    private Pattern colorPattern;
    private Color foregroundColor;
    private Color backgroundColor;
    
    public Config() {
        logger = AppLogger.getLogger();
        colorPattern = Pattern.compile("rgba\\((\\d+),\\s*(\\d+),\\s*(\\d+),\\s*(\\d+)\\)");
    }
    
    public Properties getDefaults() {
        Properties defaultProps = new Properties();
        defaultProps.put("PATH", "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin");
        defaultProps.put("FGCOLOR", "rgba(0,0,0,255)");
        defaultProps.put("BGCOLOR", "rgba(255,255,255,255)");
        defaultProps.put("PREFIX", "%");
        defaultProps.put("EMAIL_SENDER", "");
        defaultProps.put("EMAIL_RECIPIENT", "");
        return defaultProps;
    }
 
    public void loadConfig() {
        properties = new Properties(getDefaults());
        String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".wordprocessor";
        try (FileInputStream in = new FileInputStream(path)) {
            properties.load(in);
            logger.info("Loaded properties from path " + path);
            logger.info("Properties\n" + properties.toString());
        } catch (FileNotFoundException ex) {
            logger.warning(ex.toString());
        } catch (IOException ex) {
            logger.warning(ex.toString());
        }
    }
    
    public String getPath() {
        return properties.getProperty("PATH");
    }
    
    public String[] getPaths() {
        return getPath().split(":");
    }
    
    public Color getForegroundColor() {
        if (foregroundColor == null) {
            String fgcolor = properties.getProperty("FGCOLOR");

            Matcher matcher = colorPattern.matcher(fgcolor);
            if (matcher.matches()) {
                int[] rgba = new int[4];
                rgba[0] = Integer.parseInt(matcher.group(1));
                rgba[1] = Integer.parseInt(matcher.group(2));
                rgba[2] = Integer.parseInt(matcher.group(3));
                rgba[3] = Integer.parseInt(matcher.group(4));
                foregroundColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
            }
            else
                foregroundColor = new Color(0, 0, 0, 255);
        }
        return foregroundColor;
    }
    
    public Color getBackgroundColor() {
        if (backgroundColor == null) {
            String bgcolor = properties.getProperty("BGCOLOR");   

            Matcher matcher = colorPattern.matcher(bgcolor);
            if (matcher.matches()) {
                int[] rgba = new int[4];
                rgba[0] = Integer.parseInt(matcher.group(1));
                rgba[1] = Integer.parseInt(matcher.group(2));
                rgba[2] = Integer.parseInt(matcher.group(3));
                rgba[3] = Integer.parseInt(matcher.group(4));
                backgroundColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
            }
            else
                backgroundColor = new Color(255, 255, 255, 255);
        }
        return backgroundColor;
    }
    
    public String getPrefix() {
        return properties.getProperty("PREFIX");
    }
    
    public String getEmailSender() {
        return properties.getProperty("EMAIL_SENDER");
    }
    
    public String getEmailRecipient() {
        return properties.getProperty("EMAIL_RECIPIENT");
    }
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    public Properties getProperties() {
        return properties;
    }
}
