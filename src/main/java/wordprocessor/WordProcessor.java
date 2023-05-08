package wordprocessor;

import java.awt.BorderLayout;
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
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;

public class WordProcessor extends JFrame implements ActionListener, MenuListener {

    private JMenuBar bar;
    private JMenu fileMenu;
    private JMenuItem create, save, saveAs, open, preferences, exit;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private SettingsDialog settingsDialog;
    private File file;
    
    public WordProcessor() {
        super("Word Processor");
    }
    
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            System.err.println(e);
        }
    }
        
    public void createAndShowGui() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLookAndFeel();
        bar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.addMenuListener(this);
        create = new JMenuItem("New");
        create.addActionListener(this);
        save = new JMenuItem("Save");
        save.addActionListener(this);
        saveAs = new JMenuItem("Save as");
        saveAs.addActionListener(this);
        open = new JMenuItem("Open");
        open.addActionListener(this);
        preferences = new JMenuItem("Preferences");
        preferences.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        fileMenu.add(create);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(open);
        fileMenu.add(preferences);
        fileMenu.add(exit);
        bar.add(fileMenu);
        setJMenuBar(bar);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane);
        setContentPane(contentPane);
        fileChooser = new JFileChooser();
        settingsDialog = new SettingsDialog(this);
        setupKeyListener();
        applySettings();
    }
    
    private void setupKeyListener() {
        ActionMap am = textArea.getActionMap();
        Action cmdS = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
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
    
    public void applySettings() {
        Settings settings = Settings.getInstance();
        textArea.setForeground(settings.getForeground());
        textArea.setBackground(settings.getBackground());
        textArea.setTabSize(settings.getTabSize());
        textArea.setFont(settings.getFont());
    }
        
    public void newFile() {
        file = null;
        textArea.setText("");
    }
    
    public void saveFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            String text = textArea.getText();
            bufferedWriter.write(text);
        } catch (IOException e) {
            System.err.println(e);
        } 
    }

    public void saveFileAs() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.file = fileChooser.getSelectedFile();
            saveFile();
        }
    }

    public void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.file = fileChooser.getSelectedFile();
            try {
                String text = Files.readString(file.toPath());
                textArea.setText(text);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create) {
            newFile();
        } else if (e.getSource() == save) {
            saveFile();
        } else if (e.getSource() == saveAs) {
            saveFileAs();
        } else if (e.getSource() == open) {
            openFile();
        } else if (e.getSource() == preferences) {
            settingsDialog.showDialog();
        } else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } 
    }
    
    @Override
    public void menuSelected(MenuEvent e) {
        if (file == null) {
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
        Settings settings = Settings.getInstance();
        settings.load(new File(System.getProperty("user.home"), ".wordprocessor"));
        WordProcessor wordProcessor = new WordProcessor();
        wordProcessor.createAndShowGui();
        wordProcessor.setVisible(true);
    }
}