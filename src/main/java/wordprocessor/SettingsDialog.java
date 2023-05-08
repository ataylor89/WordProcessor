package wordprocessor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author andrewtaylor
 */
public class SettingsDialog extends JDialog implements ActionListener {
    
    private final Settings settings;
    private JPanel contentPane;
    private JComboBox chooseTheme, chooseTabSize, chooseFontFamily, chooseFontSize;
    private JButton chooseFgColor, chooseBgColor, save, cancel;
    
    public SettingsDialog(Frame frame) {
        super(frame, true);
        super.setTitle("Settings");
        super.setSize(500, 500);
        this.settings = Settings.getInstance(null);
        createDialog();
    }
    
    private void createDialog() {
        contentPane = new JPanel();        
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(40, 20, 0, 0);
        addComponent(new JLabel("Theme:"), c, 0, 0, 1, 1);
        String[] themes = new String[] {"Sea Theme", "Purple White"};
        chooseTheme = new JComboBox(themes);
        addComponent(chooseTheme, c, 1, 0, 9, 1);
        c.insets = new Insets(0, 20, 0, 0);
        addComponent(new JLabel("Foreground color:"), c, 0, 1, 1, 1);
        chooseFgColor = new JButton();
        chooseFgColor.setPreferredSize(new Dimension(24, 24));
        chooseFgColor.addActionListener(this);
        addComponent(chooseFgColor, c, 1, 1, 9, 1);
        addComponent(new JLabel("Background color:"), c, 0, 2, 1, 1);
        chooseBgColor = new JButton();
        chooseBgColor.setPreferredSize(new Dimension(24, 24));
        chooseBgColor.addActionListener(this);
        addComponent(chooseBgColor, c, 1, 2, 9, 1);
        addComponent(new JLabel("Tab size:"), c, 0, 3, 1, 1);
        String[] tabSizes = new String[] {"1", "2", "3", "4", "5", "6", "7", "8"};
        chooseTabSize = new JComboBox(tabSizes);
        addComponent(chooseTabSize, c, 1, 3, 9, 1);
        addComponent(new JLabel("Font family:"), c, 0, 4, 1, 1);
        String[] fonts = new String[] {"SansSerif", "Serif", "Courier New", "Times New Roman"};
        chooseFontFamily = new JComboBox(fonts);
        addComponent(chooseFontFamily, c, 1, 4, 9, 1);
        addComponent(new JLabel("Font size:"), c, 0, 5, 1, 1);
        String[] fontSizes = new String[] {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
        chooseFontSize = new JComboBox(fontSizes);
        addComponent(chooseFontSize, c, 1, 5, 9, 1);
        save = new JButton("Save");
        super.getRootPane().setDefaultButton(save);
        save.addActionListener(this);
        addComponent(save, c, 2, 9, 1, 1);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        addComponent(cancel, c, 3, 9, 1, 1);
        this.setContentPane(contentPane);
    }
    
    private void addComponent(Component cmp, GridBagConstraints cs, int gridx, int gridy, int weightx, int weighty) {
        cs.gridx = gridx;
        cs.gridy = gridy;
        cs.weightx = weightx;
        cs.weighty = weighty;
        contentPane.add(cmp, cs);
    }
    
    public void showDialog() {
        Font font = settings.getFont();
        Color fgcolor = settings.getForeground();
        Color bgcolor = settings.getBackground();
        int tabSize = settings.getTabSize();
        chooseFontFamily.setSelectedItem(font.getFamily());
        chooseFontSize.setSelectedItem(String.valueOf(font.getSize()));
        chooseFgColor.setBackground(fgcolor);
        chooseBgColor.setBackground(bgcolor);
        chooseTabSize.setSelectedItem(String.valueOf(tabSize));
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseFgColor) {
            Color initial = settings.getForeground();
            Color choice = JColorChooser.showDialog(this, "Chooose foreground color", initial);
            chooseFgColor.setBackground(choice);
        }
        else if (e.getSource() == chooseBgColor) {
            Color initial = settings.getBackground();
            Color choice = JColorChooser.showDialog(this, "Chooose background color", initial);
            chooseBgColor.setBackground(choice);
        }
        else if (e.getSource() == save) {
            settings.setBackground(chooseBgColor.getBackground());
            settings.setForeground(chooseFgColor.getBackground());
            String fontFamily = (String) chooseFontFamily.getSelectedItem();
            Integer fontSize = Integer.parseInt((String) chooseFontSize.getSelectedItem());
            Font font = new Font(fontFamily, Font.PLAIN, fontSize);
            settings.setFont(font);
            Integer tabSize = Integer.parseInt((String) chooseTabSize.getSelectedItem());
            settings.setTabSize(tabSize);
            settings.apply();
            setVisible(false);
        }
        else if (e.getSource() == cancel) {
            setVisible(false);
        }
    }
}
