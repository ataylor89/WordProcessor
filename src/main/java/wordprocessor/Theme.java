package wordprocessor;

import java.awt.Color;
import java.util.stream.Stream;

/**
 *
 * @author andrewtaylor
 */
public enum Theme {
    
    SEA ("Sea Theme", new Color(255,255,255), new Color(0,153,255)),
    WHITE_PURPLE ("White Purple", new Color(255,255,255), new Color(153,0,153)),
    BLUE_GRAY ("Blue Gray", new Color(0,0,204), new Color(204,204,204)),
    CUSTOM ("Custom", null, null);
    
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
        return Stream.of(values())
                .filter(theme -> theme.getName().equals(name))
                .findFirst().orElse(null);
    }
    
    public static String[] getNames() {
        return Stream.of(values())
                .map(theme -> theme.getName())
                .toArray(n -> new String[n]);
    }
}
