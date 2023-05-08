package wordprocessor;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;

/**
 *
 * @author andrewtaylor
 */
public class Settings {
    
    private static Settings instance;
    private final WordProcessor wp;
    private Font font;
    private Color foreground;
    private Color background;
    private int tabSize;
    private final Pattern fontPattern, colorPattern;
    
    private Settings(WordProcessor wp) {
        this.wp = wp;
        font = new Font("SansSerif", Font.PLAIN, 12);
        foreground = Color.BLACK;
        background = Color.WHITE;
        tabSize = 4;
        fontPattern = Pattern.compile("([a-zA-Z1-9 ]+),(\\d+)");
        colorPattern = Pattern.compile("rgba\\((\\d+),\\s*(\\d+),\\s*(\\d+),\\s*(\\d+)\\)");
    }
    
    public static Settings getInstance(WordProcessor wp) {
        if (instance == null) {
            instance = new Settings(wp);
        }
        return instance;
    }
     
    public void load(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(in);
            if (properties.containsKey("FONT")) {
                String selection = properties.getProperty("FONT");
                Matcher matcher = fontPattern.matcher(selection);
                if (matcher.matches()) {
                    String fontFamily = matcher.group(1);
                    Integer fontSize = Integer.parseInt(matcher.group(2));
                    font = new Font(fontFamily, Font.PLAIN, fontSize);
                }
            }
            if (properties.containsKey("FOREGROUND_COLOR")) {
                String fgcolor = properties.getProperty("FOREGROUND_COLOR");
                Matcher matcher = colorPattern.matcher(fgcolor);
                if (matcher.matches()) {
                    int[] rgba = new int[4];
                    rgba[0] = Integer.parseInt(matcher.group(1));
                    rgba[1] = Integer.parseInt(matcher.group(2));
                    rgba[2] = Integer.parseInt(matcher.group(3));
                    rgba[3] = Integer.parseInt(matcher.group(4));
                    foreground = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
                }
            }
            if (properties.containsKey("BACKGROUND_COLOR")) {
                String bgcolor = properties.getProperty("BACKGROUND_COLOR");   
                Matcher matcher = colorPattern.matcher(bgcolor);
                if (matcher.matches()) {
                    int[] rgba = new int[4];
                    rgba[0] = Integer.parseInt(matcher.group(1));
                    rgba[1] = Integer.parseInt(matcher.group(2));
                    rgba[2] = Integer.parseInt(matcher.group(3));
                    rgba[3] = Integer.parseInt(matcher.group(4));
                    background = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
                }
            }
            if (properties.containsKey("TAB_SIZE")) {
                tabSize = Integer.parseInt(properties.getProperty("TAB_SIZE"));
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    public Font getFont() {
        return font;
    }
    
    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }
    
    public Color getForeground() {       
        return foreground;
    }
    
    public void setBackground(Color background) {
        this.background = background;
    }
    
    public Color getBackground() {
        return background;
    }
    
    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }
    
    public int getTabSize() {
        return tabSize;
    }
    
    public void apply() {
        JTextArea textArea = wp.getTextArea();
        textArea.setForeground(foreground);
        textArea.setBackground(background);
        textArea.setTabSize(tabSize);
        textArea.setFont(font);
    }
}
