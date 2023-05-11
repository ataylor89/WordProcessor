package wordprocessor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author andrewtaylor
 */
public class PreferencesDialog extends JDialog implements ActionListener {
    
    private JPanel contentPane;
    private JComboBox chooseTheme, chooseTabSize, chooseFontFamily;
    private JSpinner chooseFontSize;
    private JLabel directoryPath;
    private ColorSample chooseFgColor, chooseBgColor;
    private JButton chooseDirectory;
    private JFileChooser fileChooser;
    private JButton apply, cancel;
    
    public PreferencesDialog(Frame frame) {
        super(frame, true);
        init();
    }
    
    private void init() {
        setTitle("Preferences");
        setSize(800, 600);
        contentPane = new JPanel();        
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Preferences preferences = Preferences.getInstance();
        
        JLabel themeLabel = new JLabel("Theme:");
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(20, 20, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        contentPane.add(themeLabel, c);
                
        String[] themes = Theme.getNames();
        chooseTheme = new JComboBox(themes);
        chooseTheme.addActionListener(this);
        c.gridx = 1;
        c.weightx = 8;
        contentPane.add(chooseTheme, c);
        
        JLabel fgcolorLabel = new JLabel("Foreground color:");
        c.insets = new Insets(0, 20, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        contentPane.add(fgcolorLabel, c);
        
        chooseFgColor = new ColorSample(preferences.getForeground(), 24, 24);
        chooseFgColor.addActionListener(this);
        c.gridx = 1;
        contentPane.add(chooseFgColor, c);
        
        JLabel bgcolorLabel = new JLabel("Background color:");
        c.gridx = 0;
        c.gridy = 2;
        contentPane.add(bgcolorLabel, c);
              
        chooseBgColor = new ColorSample(preferences.getBackground(), 24, 24);
        chooseBgColor.addActionListener(this);
        c.gridx = 1;
        contentPane.add(chooseBgColor, c);
        
        JLabel tabSizeLabel = new JLabel("Tab size:");
        c.gridx = 0;
        c.gridy = 3;
        contentPane.add(tabSizeLabel, c);
        
        String[] tabSizes = new String[] {"1", "2", "3", "4", "5", "6", "7", "8"};
        chooseTabSize = new JComboBox(tabSizes);
        c.gridx = 1;
        contentPane.add(chooseTabSize, c);
        
        JLabel fontFamilyLabel = new JLabel("Font family:");
        c.gridx = 0;
        c.gridy = 4;
        contentPane.add(fontFamilyLabel, c);
        
        String[] fonts = new String[] {"Arial", "Comic Sans MS", "Courier New", "Open Sans", "SansSerif", "Serif", "Times New Roman"};
        chooseFontFamily = new JComboBox(fonts);
        c.gridx = 1;
        contentPane.add(chooseFontFamily, c);
        
        JLabel fontSizeLabel = new JLabel("Font size:");
        c.gridx = 0;
        c.gridy = 5;
        contentPane.add(fontSizeLabel, c);
             
        int fontSize = preferences.getFont().getSize();
        SpinnerNumberModel model = new SpinnerNumberModel(fontSize, 12, 48, 1);
        chooseFontSize = new JSpinner(model);
        c.gridx = 1;
        contentPane.add(chooseFontSize, c);
        
        JLabel directoryLabel = new JLabel("Current directory:");
        c.gridx = 0;
        c.gridy = 6;
        contentPane.add(directoryLabel, c);
        
        directoryPath = new JLabel(preferences.getDirectory().getPath());
        c.gridx = 1;
        contentPane.add(directoryPath, c);
        
        chooseDirectory = new JButton("Browse");
        chooseDirectory.addActionListener(this);
        c.gridx = 2;
        c.weightx = 8;
        contentPane.add(chooseDirectory, c);
            
        apply = new JButton("Apply");
        super.getRootPane().setDefaultButton(apply);
        apply.addActionListener(this);
        c.gridx = 3;
        c.gridy = 8;
        c.weightx = 1;
        contentPane.add(apply, c);
        
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        c.gridx = 4;
        c.insets = new Insets(0, 20, 0, 20);
        contentPane.add(cancel, c);
        
        fileChooser = new JFileChooser(preferences.getDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.setContentPane(contentPane);
        setDefaults();
    }
        
    public void setDefaults() {
        Preferences preferences = Preferences.getInstance();
        Theme theme = preferences.getTheme();
        Color fgcolor = preferences.getForeground();
        Color bgcolor = preferences.getBackground();
        Font font = preferences.getFont();
        int tabSize = preferences.getTabSize();
        chooseTheme.setSelectedItem(theme.getName());
        chooseFontFamily.setSelectedItem(font.getFamily());
        chooseFontSize.setValue(font.getSize());
        chooseFgColor.setColor(fgcolor);
        chooseBgColor.setColor(bgcolor);
        chooseFgColor.repaint();
        chooseBgColor.repaint();
        chooseTabSize.setSelectedItem(String.valueOf(tabSize));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Preferences preferences = Preferences.getInstance();
        if (e.getSource() == chooseTheme) {
            String name = (String) chooseTheme.getSelectedItem();
            if (!name.equals("Custom")) {
                Theme theme = Theme.forName(name);
                chooseFgColor.setColor(theme.getForeground());
                chooseBgColor.setColor(theme.getBackground());
                chooseFgColor.repaint();
                chooseBgColor.repaint();
            }
        }
        else if (e.getSource() == chooseFgColor) {
            Color initial = preferences.getForeground();
            Color choice = JColorChooser.showDialog(this, "Chooose foreground color", initial);
            if (choice != null) {
                chooseFgColor.setColor(choice);
                chooseFgColor.repaint();
                chooseTheme.setSelectedItem("Custom");
            }
        }
        else if (e.getSource() == chooseBgColor) {
            Color initial = preferences.getBackground();
            Color choice = JColorChooser.showDialog(this, "Chooose background color", initial);
            if (choice != null) {
                chooseBgColor.setColor(choice);
                chooseBgColor.repaint();
                chooseTheme.setSelectedItem("Custom");
            }
        }
        else if (e.getSource() == chooseDirectory) {
            if (fileChooser.showDialog(this, "Choose") == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getPath();
                directoryPath.setText(path);
            }
        }
        else if (e.getSource() == apply) {
            String themeName = (String) chooseTheme.getSelectedItem();
            Theme theme = Theme.forName(themeName);
            preferences.setTheme(theme);
            preferences.setForeground(chooseFgColor.getColor());
            preferences.setBackground(chooseBgColor.getColor());
            Integer tabSize = Integer.parseInt((String) chooseTabSize.getSelectedItem());
            preferences.setTabSize(tabSize);
            String fontFamily = (String) chooseFontFamily.getSelectedItem();
            Integer fontSize = (Integer) chooseFontSize.getValue();
            Font font = new Font(fontFamily, Font.PLAIN, fontSize);
            preferences.setFont(font);
            File directory = fileChooser.getSelectedFile();
            if (directory != null) {
                preferences.setDirectory(directory);
            }
            preferences.apply();
            setVisible(false);
        }
        else if (e.getSource() == cancel) {
            setVisible(false);
            setDefaults();
        }
    }
}
