package wordprocessor;

import java.awt.Color;

/**
 *
 * @author andrewtaylor
 */
public enum Theme {
    
    SEA ("Sea Theme", new Color(255,255,255), new Color(0,153,255)),
    WHITE_PURPLE ("White Purple", new Color(255,255,255), new Color(153,0,153)),
    BLUE_GRAY ("Blue Gray", new Color(0,0,204), new Color(204,204,204)),
    PURPLE_WHITE ("Purple White", new Color(153,0,153), new Color(255,255,255)),
    GRAY_BLUE ("Gray Blue", new Color(204,204,204), new Color(0,0,204));
    
    private final String name;
    private final Color foreground, background;
    
    Theme(String name, Color foreground, Color background) {
        this.name = name;
        this.foreground = foreground;
        this.background = background;
    }
    
    public String getName() {
        return name;
    }
    
    public Color getForeground() {
        return foreground;
    }
    
    public Color getBackground() {
        return background;
    }
        
    public static Theme forName(String name) {
        for (Theme theme : Theme.values()) {
            if (theme.getName().equals(name)) {
                return theme;
            }
        }
        return null;
    }
    
    public static String[] getNames() {
        Theme[] themes = values();
        String[] names = new String[themes.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = themes[i].getName();
        }
        return names;
    }
}
