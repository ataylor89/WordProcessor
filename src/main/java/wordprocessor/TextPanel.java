package wordprocessor;

import java.awt.BorderLayout;
import java.awt.TextArea;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author andrewtaylor
 */
public class TextPanel extends JPanel {
    
    private WordProcessor wp;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    
    public TextPanel(WordProcessor wp) {
        super();
        this.wp = wp;
    }
    
    public void addComponents() {
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setTabSize(3);
        scrollPane = new JScrollPane(textArea);
        add(scrollPane);
    }
}
