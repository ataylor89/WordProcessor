package wordprocessor;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

/**
 *
 * @author andrewtaylor
 */
public class Settings {
    
    private static Settings instance;
    private WordProcessor wp;
    private Theme theme;
    private Color foreground;
    private Color background;
    private Font font;
    private int tabSize;
    private File directory;
    private final Pattern colorPattern, fontPattern;
    
    private Settings() {
        theme = Theme.SEA;
        tabSize = 4;
        font = new Font("SansSerif", Font.PLAIN, 12);
        directory = new File(System.getProperty("user.home"));
        colorPattern = Pattern.compile("rgba\\((\\d+),\\s*(\\d+),\\s*(\\d+),\\s*(\\d+)\\)");
        fontPattern = Pattern.compile("([a-zA-Z1-9 ]+),(\\d+)");
    }
    
    private Settings(WordProcessor wp) {
        this();
        this.wp = wp;
    }
    
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
    
    public static Settings getInstance(WordProcessor wp) {
        if (instance == null) {
            instance = new Settings(wp);
        }
        return instance;
    }
    
    public void load() {
        File file = new File(System.getProperty("user.home"), ".wordprocessor");
        if (file.exists()) {
            load(file);
        } 
        else {
            file = new File(System.getProperty("user.dir"), ".wordprocessor");
            if (file.exists()) {
                load(file);
            }
        }
    }
    
    public void load(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(in);
            if (properties.containsKey("THEME")) {
                String themeName = properties.getProperty("THEME");
                theme = Theme.forName(themeName);
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
            if (properties.containsKey("FONT")) {
                String selection = properties.getProperty("FONT");
                Matcher matcher = fontPattern.matcher(selection);
                if (matcher.matches()) {
                    String fontFamily = matcher.group(1);
                    Integer fontSize = Integer.parseInt(matcher.group(2));
                    font = new Font(fontFamily, Font.PLAIN, fontSize);
                }
            }
            if (properties.containsKey("TAB_SIZE")) {
                tabSize = Integer.parseInt(properties.getProperty("TAB_SIZE"));
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
        
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
    
    public Theme getTheme() {
        return theme;
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
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    public Font getFont() {
        return font;
    }
    
    public void setDirectory(File directory) {
        this.directory = directory;
    }
    
    public File getDirectory() {
        return directory;
    }
    
    public void setGui(WordProcessor wp) {
        this.wp = wp;
    }
    
    public WordProcessor getGui() {
        return wp;
    }
    
    public void apply() {
        JTextArea textArea = wp.getTextArea();
        textArea.setForeground(foreground);
        textArea.setBackground(background);
        textArea.setTabSize(tabSize);
        textArea.setFont(font);
        JFileChooser fileChooser = wp.getFileChooser();
        fileChooser.setCurrentDirectory(directory);
    }
}
