package wordprocessor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class WordProcessor extends JFrame implements ActionListener, MenuListener {

    private JMenuBar bar;
    private JMenu file;
    private JMenuItem newFile, save, saveAs, open, exit;
    private JMenu colors;
    private JMenuItem fgcolor, bgcolor, whiteblack, grayblue, tealwhite, purplewhite, seaTheme;
    private JMenu tools;
    private JMenuItem setTabSize, lineCount, characterCount, copyToClipboard;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private Config config;
    
    public WordProcessor() {
        super("Word Processor");
        config = new Config();
        config.loadConfig();
    }
    
    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            System.err.println(e);
        }
    }
        
    public void createAndShowGui() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bar = new JMenuBar();
        file = new JMenu("File");
        file.addMenuListener(this);
        newFile = new JMenuItem("New");
        newFile.addActionListener(this);
        save = new JMenuItem("Save");
        save.addActionListener(this);
        saveAs = new JMenuItem("Save as");
        saveAs.addActionListener(this);
        open = new JMenuItem("Open");
        open.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        file.add(newFile);
        file.add(save);
        file.add(saveAs);
        file.add(open);
        file.add(exit);
        colors = new JMenu("Colors");
        fgcolor = new JMenuItem("Set foreground color");
        fgcolor.addActionListener(this);
        bgcolor = new JMenuItem("Set background color");
        bgcolor.addActionListener(this);
        whiteblack = new JMenuItem("White black");
        whiteblack.addActionListener(this);
        grayblue = new JMenuItem("Gray blue");
        grayblue.addActionListener(this);
        tealwhite = new JMenuItem("Teal white");
        tealwhite.addActionListener(this);
        purplewhite = new JMenuItem("Purple white");
        purplewhite.addActionListener(this);
        seaTheme = new JMenuItem("Sea theme");
        seaTheme.addActionListener(this);
        colors.add(fgcolor);
        colors.add(bgcolor);
        colors.add(whiteblack);
        colors.add(grayblue);
        colors.add(tealwhite);
        colors.add(purplewhite);
        colors.add(seaTheme);
        tools = new JMenu("Tools");
        setTabSize = new JMenuItem("Set tab size");
        setTabSize.addActionListener(this);
        lineCount = new JMenuItem("Get line count");
        lineCount.addActionListener(this);
        characterCount = new JMenuItem("Get character count");
        characterCount.addActionListener(this);
        copyToClipboard = new JMenuItem("Copy text to clipboard");
        copyToClipboard.addActionListener(this);
        tools.add(setTabSize);
        tools.add(lineCount);
        tools.add(characterCount);
        tools.add(copyToClipboard);
        bar.add(file);
        bar.add(colors);
        bar.add(tools);
        setJMenuBar(bar);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setTabSize(2);
        textArea.setForeground(config.getForegroundColor());
        textArea.setBackground(config.getBackgroundColor());
        scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane);
        setContentPane(contentPane);
        fileChooser = new JFileChooser();  
    }
    
    public void setupKeyStrokes() {
        ActionMap am = textArea.getActionMap();
        Action cmdS = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        };
        Action cmdO = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        };
        am.put("cmd+s", cmdS);
        am.put("cmd+o", cmdO);
        InputMap im = textArea.getInputMap();
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.META_DOWN_MASK), "cmd+s");
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.META_DOWN_MASK), "cmd+o");
    }

    private boolean isSaved() {
        try {
            String fileContents = Files.readString(currentFile.toPath());
            if (textArea.getText().equals(fileContents))
                return true;
        } catch (IOException e) {
            System.err.println(e);
        }
        return false;
    }
    
    private void promptForSave() {
        if (currentFile != null && !isSaved()) {
            int option = JOptionPane.showConfirmDialog(this, "Would you like to save the current file?");
            if (option == JOptionPane.YES_OPTION) 
                saveToFile(currentFile);
        }
    }

    private void newFile() {
        currentFile = null;
        textArea.setText("");
    }

    private void saveToFile() {
        if (currentFile == null)
            saveToFileAs();
        else
            saveToFile(currentFile);
    }
    
    private void saveToFile(File file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            String text = textArea.getText();
            bufferedWriter.write(text);
        } catch (IOException e) {
            System.err.println(e);
        } 
    }

    private void saveToFileAs() {
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            saveToFile(currentFile);
        }
    }

    private void openFile(File file) {
        try {
            String text = Files.readString(file.toPath());
            textArea.setText(text);
            currentFile = file;
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void openFile() {
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            openFile(f);
        }
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFile) {
            promptForSave();
            newFile();
        } else if (e.getSource() == save) {
            saveToFile(currentFile);
        } else if (e.getSource() == saveAs) {
            saveToFileAs();
        } else if (e.getSource() == open) {
            promptForSave();
            openFile();
        } else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } else if (e.getSource() == fgcolor) {
            Color color = JColorChooser.showDialog(this, "Select a foreground color", textArea.getForeground());
            if (color != null) 
                textArea.setForeground(color);
        } else if (e.getSource() == bgcolor) {
            Color color = JColorChooser.showDialog(this, "Select a background color", textArea.getBackground());
            if (color != null) 
                textArea.setBackground(color);
        } else if (e.getSource() == whiteblack) {
            textArea.setForeground(Color.BLACK);
            textArea.setBackground(Color.WHITE);
        } else if (e.getSource() == grayblue) {
            textArea.setForeground(new Color(0, 0, 204, 255));
            textArea.setBackground(new Color(204, 204, 204, 255));
        } else if (e.getSource() == tealwhite) {
            textArea.setForeground(Color.WHITE);
            textArea.setBackground(new Color(0, 153, 153, 255));
        } else if (e.getSource() == purplewhite) {
            textArea.setForeground(Color.WHITE);
            textArea.setBackground(new Color(153, 0, 153, 255));
        } else if (e.getSource() == seaTheme) {
            textArea.setForeground(Color.WHITE);
            textArea.setBackground(new Color(0, 153, 255, 255));
        } else if (e.getSource() == setTabSize) {
            Integer[] tabSizes = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            int tabSize = (Integer) JOptionPane.showInputDialog(this, "Select tab size", "Tab size", JOptionPane.QUESTION_MESSAGE, null, tabSizes, textArea.getTabSize());
            textArea.setTabSize(tabSize);
        } else if (e.getSource() == lineCount) {
            JOptionPane.showMessageDialog(this, "There are " + textArea.getLineCount() + " lines in the file");
        } else if (e.getSource() == characterCount) {
            JOptionPane.showMessageDialog(this, "There are " + textArea.getText().length() + " characters in the file");
        } else if (e.getSource() == copyToClipboard) {
            StringSelection stringSelection = new StringSelection(textArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }
    
    @Override
    public void menuSelected(MenuEvent e) {
        if (currentFile == null) {
            save.setEnabled(false);
        }
        else {
            save.setEnabled(true);
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}
    
    public static void main(String[] args) {
        WordProcessor wordProcessor = new WordProcessor();
        wordProcessor.setLookAndFeel();
        wordProcessor.createAndShowGui();
        wordProcessor.setupKeyStrokes();
        wordProcessor.setVisible(true);
    }
}