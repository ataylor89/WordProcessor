package wordprocessor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;

public class WordProcessor extends JFrame implements ActionListener {

    private MenuBar menuBar;
    private JPanel panel;
    private JScrollPane scrollPane;
    private TextArea textEditor;
    private File currentFile;
    private Color foregroundColor, backgroundColor;
    private int tabWidth;

    private class ProcessController extends Thread {

        private JFrame frame;
        private String title;
        private String cmd;
        private JDialog dialog;
        private JTextArea display;

        public ProcessController(JFrame frame, String title, String cmd) {
            this.frame = frame;
            this.title = title;
            this.cmd = cmd;
        }

        private void createAndShowDialog() {
            dialog = new JDialog(frame, title);
            dialog.setSize(500, 500);
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            display = new JTextArea();
	    display.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(display);
            panel.add(scrollPane);
            dialog.add(panel);
            dialog.setVisible(true);
        }

        private void runCommand() {
            try {
                display.append(cmd + "\n");
                Process process = Runtime.getRuntime().exec(cmd);
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader bre = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                int value;
                while (dialog.isShowing() && process.isAlive()) {
                    br.lines().forEach(line -> display.append(line + "\n"));
                    bre.lines().forEach(line -> display.append("Error: " + line + "\n"));
                }
                process.destroy();
            } catch (IOException e) {
                display.append(e + "\n");
            }
        }

        @Override
        public void run() {
            createAndShowDialog();
            runCommand();
        }
    }

    private class EmailPanel extends JPanel {

        private GridBagLayout gridbag;
        private JTextField from, to, subject;
        private JPasswordField password;

        public EmailPanel() {
            gridbag = new GridBagLayout();
            setLayout(gridbag);
            JLabel fromLabel = new JLabel("From: ");
            from = new JTextField();
            JLabel toLabel = new JLabel("To: ");
            to = new JTextField();
            JLabel subjectLabel = new JLabel("Subject: ");
            subject = new JTextField();
            JLabel passwordLabel = new JLabel("Password: ");
            password = new JPasswordField(20);
            addComponent(fromLabel, 0, 0, 1, 1, 10, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
            addComponent(from, 1, 0, 9, 1, 90, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
            addComponent(toLabel, 0, 1, 1, 1, 10, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
            addComponent(to, 1, 1, 9, 1, 90, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
            addComponent(subjectLabel, 0, 2, 1, 1, 10, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
            addComponent(subject, 1, 2, 9, 1, 90, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
            addComponent(passwordLabel, 0, 3, 1, 1, 10, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
            addComponent(password, 1, 3, 9, 1, 90, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        }

        private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty, int fill, int anchor) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = gridx;
            constraints.gridy = gridy;
            constraints.gridwidth = gridwidth;
            constraints.gridheight = gridheight;
            constraints.weightx = weightx;
            constraints.weighty = weighty;
            constraints.fill = fill;
            constraints.anchor = anchor;
            gridbag.setConstraints(component, constraints);
            add(component);
        }

        public String getFrom() {
            return from.getText();
        }

        public String getTo() {
            return to.getText();
        }

        public String getSubject() {
            return subject.getText();
        }

        public String getPassword() {
            return new String(password.getPassword());
        }
    }

    private class JavaPanel extends JPanel {

        private GridBagLayout gridbag;
        private JTextField classpath, args;

        public JavaPanel() {
            gridbag = new GridBagLayout();
            setLayout(gridbag);
            JLabel classpathLabel = new JLabel("Classpath: ");
            classpath = new JTextField(20);
            JLabel argsLabel = new JLabel("Args: ");
            args = new JTextField(20);
            addComponent(classpathLabel, 0, 0, 1, 1, 10, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
            addComponent(classpath, 1, 0, 9, 1, 90, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
            addComponent(argsLabel, 0, 1, 1, 1, 10, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
            addComponent(args, 1, 1, 9, 1, 90, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        }

        private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty, int fill, int anchor) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = gridx;
            constraints.gridy = gridy;
            constraints.gridwidth = gridwidth;
            constraints.gridheight = gridheight;
            constraints.weightx = weightx;
            constraints.weighty = weighty;
            constraints.fill = fill;
            constraints.anchor = anchor;
            gridbag.setConstraints(component, constraints);
            add(component);
        }

        public String getClasspath() {
            return classpath.getText();
        }

        public String getArgs() {
            return args.getText();
        }
    }

    public WordProcessor() {
        super("Word Processor");
        tabWidth = 1;
    }

    public void setFilePath(String path) {
        currentFile = new File(path);
    }

    public void createAndShowGui() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        setJMenuBar(new MenuBar(this));      
        add(new TextPanel(this));
        if (currentFile != null) 
            openFile(currentFile);
    }

    public static void setupKeyStrokes() {
	InputMap im = (InputMap) UIManager.get("TextArea.focusInputMap");
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
	setupKeyStrokes();
    }

    private void setLookAndFeel(LookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
	setupKeyStrokes();
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
	setupKeyStrokes();
    }
    
    public void setFile(String filename) {
        currentFile = new File(filename);
    }
    
    public void setFile(File file) {
        currentFile = file;
    }
    
    public File getFile() {
        return currentFile;
    }
    
    public void setColors(Color foregroundColor, Color backgroundColor) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        textArea.setForeground(foregroundColor);
        textArea.setBackground(backgroundColor);
    }

    private void promptForSave() {
        if (currentFile != null) {
            int option = JOptionPane.showConfirmDialog(this, "Would you like to save the current file?");
            if (option == JOptionPane.YES_OPTION) {
                saveToFile(currentFile);
            }
        }
    }

    private void newFile() {
        currentFile = null;
        textArea.setText("");
    }

    private void saveToFile(File file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            String text = textArea.getText();
            bufferedWriter.write(text);
        } catch (IOException ex) {
            System.err.println(ex);
        } 
    }

    private void saveToFileAs() {
        int returnVal = fileChooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            saveToFile(currentFile);
        }
    }

    private void openFile(File file) {
        try {
            String text = new String(Files.readAllBytes(file.toPath()));
            textArea.setText(text);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File  = fileChooser.getSelectedFile();
            openFile(currentFile);
        }
    }

    public static void main(String[] args) {
        WordProcessor wp = new WordProcessor();
        wp.setLookAndFeel();
        if (args.length > 0) 
            wp.setFile(args[0]);
        wp.createAndShowGui();
        wp.setVisible(true);
    }
}
