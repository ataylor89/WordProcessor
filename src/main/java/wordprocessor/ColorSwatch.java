package wordprocessor;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;

/**
 *
 * @author andrewtaylor
 */
public class ColorSwatch extends JButton {
    
    public ColorSwatch() {
        super();
        super.setPreferredSize(new Dimension(24, 24));
    }
        
    public void setColor(Color color) {
        setBackground(color);
    }
    
    public Color getColor() {
        return getBackground();
    }
}
