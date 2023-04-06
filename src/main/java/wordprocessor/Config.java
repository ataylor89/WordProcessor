package wordprocessor;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author andrewtaylor
 */
public class Config {
    
    private Color foregroundColor;
    private Color backgroundColor;
    private int tabSize;
    private final Pattern pattern;
    private final Properties properties;
    private final File file;
    
    public Config() {
        pattern = Pattern.compile("rgba\\((\\d+),\\s*(\\d+),\\s*(\\d+),\\s*(\\d+)\\)");
        file = new File(System.getProperty("user.home"), ".wordprocessor");
        Properties defaultProps = new Properties();
        defaultProps.put("FGCOLOR", "rgba(0,0,0,255)");
        defaultProps.put("BGCOLOR", "rgba(255,255,255,255)");
        defaultProps.put("TAB_SIZE", "4");
        properties = new Properties(defaultProps);
    }
     
    public void load() {
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    public Color getForegroundColor() {
        if (foregroundColor == null) {
            String fgcolor = properties.getProperty("FGCOLOR");
            Matcher matcher = pattern.matcher(fgcolor);
            if (matcher.matches()) {
                int[] rgba = new int[4];
                rgba[0] = Integer.parseInt(matcher.group(1));
                rgba[1] = Integer.parseInt(matcher.group(2));
                rgba[2] = Integer.parseInt(matcher.group(3));
                rgba[3] = Integer.parseInt(matcher.group(4));
                foregroundColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
            }
            else {
                foregroundColor = new Color(0, 0, 0, 255);
            }
        }
        return foregroundColor;
    }
    
    public Color getBackgroundColor() {
        if (backgroundColor == null) {
            String bgcolor = properties.getProperty("BGCOLOR");   
            Matcher matcher = pattern.matcher(bgcolor);
            if (matcher.matches()) {
                int[] rgba = new int[4];
                rgba[0] = Integer.parseInt(matcher.group(1));
                rgba[1] = Integer.parseInt(matcher.group(2));
                rgba[2] = Integer.parseInt(matcher.group(3));
                rgba[3] = Integer.parseInt(matcher.group(4));
                backgroundColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
            }
            else {
                backgroundColor = new Color(255, 255, 255, 255);
            }
        }
        return backgroundColor;
    }
    
    public int getTabSize() {
        if (tabSize == 0) {
            tabSize = Integer.parseInt(properties.getProperty("TAB_SIZE"));
        }
        return tabSize;
    }
}
