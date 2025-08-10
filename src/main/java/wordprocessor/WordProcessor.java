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
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;

public class WordProcessor extends JFrame implements ActionListener {

    private JMenuBar bar;
    private JMenu fileMenu;
    private JMenuItem newFile, saveFile, saveFileAs, openFile, editPreferences, exit;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private PreferencesDialog preferencesDialog;
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
        newFile = new JMenuItem("New");
        newFile.addActionListener(this);
        saveFile = new JMenuItem("Save");
        saveFile.addActionListener(this);
        saveFile.setEnabled(false);
        saveFileAs = new JMenuItem("Save as");
        saveFileAs.addActionListener(this);
        openFile = new JMenuItem("Open");
        openFile.addActionListener(this);
        editPreferences = new JMenuItem("Preferences");
        editPreferences.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        fileMenu.add(newFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveFileAs);
        fileMenu.add(openFile);
        fileMenu.add(editPreferences);
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
        setupKeyListener();
        Preferences preferences = Preferences.getInstance();
        preferences.setGui(this);
        preferences.load();
        preferences.apply();
        preferencesDialog = new PreferencesDialog(this);
        setVisible(true);
    }
    
    private void setupKeyListener() {
        ActionMap am = textArea.getActionMap();
        Action cmdA = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.selectAll();
            }
        };
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
        am.put("cmd+a", cmdA);
        am.put("cmd+s", cmdS);
        am.put("cmd+o", cmdO);
        InputMap im = textArea.getInputMap();
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.META_DOWN_MASK), "cmd+a");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.META_DOWN_MASK), "cmd+s");
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.META_DOWN_MASK), "cmd+o");
    }
    
    public JTextArea getTextArea() {
        return textArea;
    }
    
    public JFileChooser getFileChooser() {
        return fileChooser;
    }
    
    public void newFile() {
        file = null;
        textArea.setText("");
        saveFile.setEnabled(false);
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
            saveFile.setEnabled(true);
        }
    }

    public void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.file = fileChooser.getSelectedFile();
            openFile(file);
        }
    }

    public void openFile(File file) {
        if (file.exists()) {
            try {
                String text = Files.readString(file.toPath());
                textArea.setText(text);
                saveFile.setEnabled(true);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFile) {
            newFile();
        } else if (e.getSource() == saveFile) {
            saveFile();
        } else if (e.getSource() == saveFileAs) {
            saveFileAs();
        } else if (e.getSource() == openFile) {
            openFile();
        } else if (e.getSource() == editPreferences) {
            preferencesDialog.setVisible(true);
        } else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } 
    }
                    
    public static void main(String[] args) {
        WordProcessor wp = new WordProcessor();
        wp.createAndShowGui();
        if (args.length == 1) {
            String filename = args[0];
            wp.openFile(new File(filename));
        }
    }
}
