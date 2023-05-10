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

/**
 *
 * @author andrewtaylor
 */
public class SettingsDialog extends JDialog implements ActionListener {
    
    private JPanel contentPane;
    private JComboBox chooseTheme, chooseTabSize, chooseFontFamily, chooseFontSize;
    private JLabel directoryPath;
    private JButton chooseFgColor, chooseBgColor, chooseDirectory;
    private JFileChooser fileChooser;
    private JButton save, cancel;
    
    public SettingsDialog(Frame frame) {
        super(frame, true);
        init();
    }
    
    private void init() {
        setTitle("Settings");
        setSize(800, 600);
        contentPane = new JPanel();        
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Settings settings = Settings.getInstance();
        
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
        
        chooseFgColor = new JButton();
        chooseFgColor.setPreferredSize(new Dimension(24, 24));
        chooseFgColor.addActionListener(this);
        c.gridx = 1;
        contentPane.add(chooseFgColor, c);
        
        JLabel bgcolorLabel = new JLabel("Background color:");
        c.gridx = 0;
        c.gridy = 2;
        contentPane.add(bgcolorLabel, c);
              
        chooseBgColor = new JButton();
        chooseBgColor.setPreferredSize(new Dimension(24, 24));
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
        
        String[] fonts = new String[] {"SansSerif", "Serif", "Courier New", "Times New Roman"};
        chooseFontFamily = new JComboBox(fonts);
        c.gridx = 1;
        contentPane.add(chooseFontFamily, c);
        
        JLabel fontSizeLabel = new JLabel("Font size:");
        c.gridx = 0;
        c.gridy = 5;
        contentPane.add(fontSizeLabel, c);
             
        String[] fontSizes = new String[] {"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
        chooseFontSize = new JComboBox(fontSizes);
        c.gridx = 1;
        contentPane.add(chooseFontSize, c);
        
        JLabel directoryLabel = new JLabel("Current directory:");
        c.gridx = 0;
        c.gridy = 6;
        contentPane.add(directoryLabel, c);
        
        directoryPath = new JLabel(settings.getDirectory().getPath());
        c.gridx = 1;
        contentPane.add(directoryPath, c);
        
        chooseDirectory = new JButton("Browse");
        chooseDirectory.addActionListener(this);
        c.gridx = 2;
        c.weightx = 8;
        contentPane.add(chooseDirectory, c);
            
        save = new JButton("Save");
        super.getRootPane().setDefaultButton(save);
        save.addActionListener(this);
        c.gridx = 3;
        c.gridy = 8;
        c.weightx = 1;
        contentPane.add(save, c);
        
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        c.gridx = 4;
        c.insets = new Insets(0, 20, 0, 20);
        contentPane.add(cancel, c);
        
        fileChooser = new JFileChooser(System.getProperty("user.home"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.setContentPane(contentPane);
        useDefaults();
    }
        
    public void useDefaults() {
        Settings settings = Settings.getInstance();
        Theme theme = settings.getTheme();
        Color fgcolor = settings.getForeground();
        Color bgcolor = settings.getBackground();
        Font font = settings.getFont();
        int tabSize = settings.getTabSize();
        chooseTheme.setSelectedItem(theme.getName());
        chooseFontFamily.setSelectedItem(font.getFamily());
        chooseFontSize.setSelectedItem(String.valueOf(font.getSize()));
        chooseFgColor.setBackground(fgcolor);
        chooseBgColor.setBackground(bgcolor);
        chooseTabSize.setSelectedItem(String.valueOf(tabSize));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Settings settings = Settings.getInstance();
        if (e.getSource() == chooseTheme) {
            String name = (String) chooseTheme.getSelectedItem();
            if (!name.equals("Custom")) {
                Theme theme = Theme.forName(name);
                chooseFgColor.setBackground(theme.getForeground());
                chooseBgColor.setBackground(theme.getBackground());
            }
        }
        else if (e.getSource() == chooseFgColor) {
            Color initial = settings.getForeground();
            Color choice = JColorChooser.showDialog(this, "Chooose foreground color", initial);
            if (choice != null) {
                chooseFgColor.setBackground(choice);
                chooseTheme.setSelectedItem("Custom");
            }
        }
        else if (e.getSource() == chooseBgColor) {
            Color initial = settings.getBackground();
            Color choice = JColorChooser.showDialog(this, "Chooose background color", initial);
            if (choice != null) {
                chooseBgColor.setBackground(choice);
                chooseTheme.setSelectedItem("Custom");
            }
        }
        else if (e.getSource() == chooseDirectory) {
            if (fileChooser.showDialog(this, "Choose") == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getPath();
                directoryPath.setText(path);
            }
        }
        else if (e.getSource() == save) {
            String themeName = (String) chooseTheme.getSelectedItem();
            Theme theme = Theme.forName(themeName);
            settings.setTheme(theme);
            settings.setBackground(chooseBgColor.getBackground());
            settings.setForeground(chooseFgColor.getBackground());
            String fontFamily = (String) chooseFontFamily.getSelectedItem();
            Integer fontSize = Integer.parseInt((String) chooseFontSize.getSelectedItem());
            Font font = new Font(fontFamily, Font.PLAIN, fontSize);
            settings.setFont(font);
            Integer tabSize = Integer.parseInt((String) chooseTabSize.getSelectedItem());
            settings.setTabSize(tabSize);
            File directory = fileChooser.getSelectedFile();
            if (directory != null) {
                settings.setDirectory(directory);
            }
            settings.apply();
            setVisible(false);
        }
        else if (e.getSource() == cancel) {
            setVisible(false);
            useDefaults();
        }
    }
}
