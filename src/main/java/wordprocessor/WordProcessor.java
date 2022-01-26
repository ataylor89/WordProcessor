package wordprocessor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.text.BadLocationException;

public class WordProcessor extends JFrame implements MenuListener, ActionListener {

    private JMenuBar bar;
    private JMenu file;
    private JMenuItem newFile, save, saveAs, open, openURL, exit;
    private JMenu colors;
    private JMenuItem fgcolor, bgcolor, whiteblack, whitegray, grayblue, tealwhite, purplewhite, seaTheme;
    private JMenu theme;
    private JMenuItem nimbus, system, metal, ocean;
    private JMenu tools;
    private JMenuItem tabSize, lineCount, characterCount, gotoLine, copyToClipboard;
    private JMenu email;
    private JMenuItem sendEmail;
    private JMenu run;
    private JMenuItem runJavaProgram, runPythonProgram;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JFileChooser fileChooser;
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
        openURL = new JMenuItem("Open URL");
        openURL.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        file.add(newFile);
        file.add(save);
        file.add(saveAs);
        file.add(open);
        file.add(openURL);
        file.add(exit);
        colors = new JMenu("Colors");
        fgcolor = new JMenuItem("Set foreground color");
        fgcolor.addActionListener(this);
        bgcolor = new JMenuItem("Set background color");
        bgcolor.addActionListener(this);
        whiteblack = new JMenuItem("White black");
        whiteblack.addActionListener(this);
        whitegray = new JMenuItem("White gray");
        whitegray.addActionListener(this);
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
        colors.add(whitegray);
        colors.add(grayblue);
        colors.add(tealwhite);
        colors.add(purplewhite);
        colors.add(seaTheme);
        theme = new JMenu("Theme");
        nimbus = new JMenuItem("Nimbus theme");
        nimbus.addActionListener(this);
        system = new JMenuItem("System theme");
        system.addActionListener(this);
        metal = new JMenuItem("Metal theme");
        metal.addActionListener(this);
        ocean = new JMenuItem("Ocean theme");
        ocean.addActionListener(this);
        theme.add(nimbus);
        theme.add(system);
        theme.add(metal);
        theme.add(ocean);
        tools = new JMenu("Tools");
        tabSize = new JMenuItem("Set tab size");
        tabSize.addActionListener(this);
        lineCount = new JMenuItem("Check line count");
        lineCount.addActionListener(this);
        characterCount = new JMenuItem("Check character count");
        characterCount.addActionListener(this);
        gotoLine = new JMenuItem("Goto line number");
        gotoLine.addActionListener(this);
        copyToClipboard = new JMenuItem("Copy text to clipboard");
        copyToClipboard.addActionListener(this);
        tools.add(tabSize);
        tools.add(lineCount);
        tools.add(characterCount);
        tools.add(gotoLine);
        tools.add(copyToClipboard);
        email = new JMenu("Email");
	email.addMenuListener(this);
        sendEmail = new JMenuItem("Send email");
        sendEmail.addActionListener(this);
        email.add(sendEmail);
        run = new JMenu("Run");
	run.addMenuListener(this);
        runJavaProgram = new JMenuItem("Run Java program");
        runJavaProgram.addActionListener(this);
        runPythonProgram = new JMenuItem("Run Python program");
        runPythonProgram.addActionListener(this);
        run.add(runJavaProgram);
        run.add(runPythonProgram);
        bar.add(file);
        bar.add(colors);
        bar.add(theme);
        bar.add(tools);
        bar.add(email);
        bar.add(run);
        setJMenuBar(bar);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setTabSize(3);
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        add(panel);
        fileChooser = new JFileChooser();
        if (currentFile != null) {
            openFile(currentFile);
        }
    }

    public static void setupKeyStrokes() {
	InputMap im = (InputMap) UIManager.get("TextArea.focusInputMap");
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
    }

    public static void setLookAndFeel() {
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
            String text = Files.readString(file.toPath());
            textArea.setText(text);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            openFile(currentFile);
        }
    }

    private void email(String to, String from, String subject, String body, String username, String password) {
        String encryptedPassword = password.substring(0, 2) + String.join("", Collections.nCopies(password.length() - 4, "*")) + password.substring(password.length() - 2);
        System.out.printf("To: %s\nFrom: %s\nSubject: %s\nUsername: %s\nPassword: %s\nBody: %s\n", to, from, subject, username, encryptedPassword, body);
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (AddressException e) {
            System.err.println(e);
        } catch (MessagingException e) {
            System.err.println(e);
        }
    }
    
    private void enableDisableMenuItems() {
        save.setEnabled(currentFile != null);
        sendEmail.setEnabled(currentFile != null);
        runJavaProgram.setEnabled(currentFile != null);
        runPythonProgram.setEnabled(currentFile != null);
    }

    @Override
    public void menuSelected(MenuEvent e) {
        enableDisableMenuItems();
    }

    @Override
    public void menuDeselected(MenuEvent e) {
    }

    @Override
    public void menuCanceled(MenuEvent e) {
    }

    private void refreshColors() {
        textArea.setForeground(foregroundColor);
        textArea.setBackground(backgroundColor);
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
        } else if (e.getSource() == openURL) {
            promptForSave();
            newFile();
            String address = JOptionPane.showInputDialog(this, "URL:", "Open URL", JOptionPane.QUESTION_MESSAGE);
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) connection.getContent()));
                String text = br.lines().collect(Collectors.joining("\n"));
                textArea.setText(text);
            } catch (MalformedURLException ex) {
                System.err.println(ex);
            } catch (IOException ex) {
                System.err.println(ex);
            }
            enableDisableMenuItems();
        } else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } else if (e.getSource() == fgcolor) {
            Color color = JColorChooser.showDialog(this, "Select a foreground color", foregroundColor);
            if (color != null) {
                foregroundColor = color;
            }
            textArea.setForeground(foregroundColor);
        } else if (e.getSource() == bgcolor) {
            Color color = JColorChooser.showDialog(this, "Select a background color", backgroundColor);
            if (color != null) {
                backgroundColor = color;
            }
            textArea.setBackground(backgroundColor);
        } else if (e.getSource() == whiteblack) {
            foregroundColor = Color.BLACK;
            backgroundColor = Color.WHITE;
            refreshColors();
        } else if (e.getSource() == whitegray) {
            foregroundColor = Color.LIGHT_GRAY;
            backgroundColor = Color.WHITE;
            refreshColors();
        } else if (e.getSource() == grayblue) {
            foregroundColor = new Color(0, 0, 204, 255);
            backgroundColor = new Color(204, 204, 204, 255);
            refreshColors();
        } else if (e.getSource() == tealwhite) {
            foregroundColor = Color.WHITE;
            backgroundColor = new Color(0, 153, 153, 255);
            refreshColors();
        } else if (e.getSource() == purplewhite) {
            foregroundColor = Color.WHITE;
            backgroundColor = new Color(153, 0, 153, 255);
            refreshColors();
        } else if (e.getSource() == seaTheme) {
            foregroundColor = Color.WHITE;
            backgroundColor = new Color(0, 153, 255, 255);
            refreshColors();
        } else if (e.getSource() == nimbus) {
            setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } else if (e.getSource() == system) {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } else if (e.getSource() == metal) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            setLookAndFeel(new MetalLookAndFeel());
        } else if (e.getSource() == ocean) {
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            setLookAndFeel(new MetalLookAndFeel());
        } else if (e.getSource() == tabSize) {
            Integer size = (Integer) JOptionPane.showInputDialog(this, "Select tab size", "Tab size", JOptionPane.QUESTION_MESSAGE, null, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, tabSize);
            tabWidth = size.intValue();
            textArea.setTabSize(tabWidth);
        } else if (e.getSource() == lineCount) {
            JOptionPane.showMessageDialog(this, "There are " + textArea.getLineCount() + " lines in the file");
        } else if (e.getSource() == characterCount) {
            JOptionPane.showMessageDialog(this, "There are " + textArea.getText().length() + " characters in the file");
        } else if (e.getSource() == gotoLine) {
            int maxLineNumber = textArea.getLineCount();
            int lineNumber = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter a line number:", "Goto line", JOptionPane.QUESTION_MESSAGE));
            if (lineNumber >= maxLineNumber) {
                lineNumber = maxLineNumber - 1;
            }
            if (lineNumber < 0) {
                lineNumber = 0;
            }
            try {
                textArea.setCaretPosition(textArea.getLineStartOffset(lineNumber));
            } catch (BadLocationException le) {
                System.err.println(le);
            }
        } else if (e.getSource() == copyToClipboard) {
            StringSelection stringSelection = new StringSelection(textArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } else if (e.getSource() == sendEmail) {
            EmailPanel panel = new EmailPanel();
            String[] options = new String[]{"Cancel", "Send"};
            int value = JOptionPane.showOptionDialog(this, panel, "Compose an email", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (value == 0) {
                return;
            }
            email(panel.getTo(), panel.getFrom(), panel.getSubject(), textArea.getText(), panel.getFrom(), panel.getPassword());
        } else if (e.getSource() == runJavaProgram) {
            String cmd = "java " + currentFile.getPath();
            ProcessController process = new ProcessController(this, "Running Java program...", cmd);
            process.start();
        } else if (e.getSource() == runPythonProgram) {
	    String cmd = "python " + currentFile.getPath();
            ProcessController process = new ProcessController(this, "Running Python program...", cmd);
            process.start();
        } 
    }

    public static void main(String[] args) {
        WordProcessor.setLookAndFeel();
        WordProcessor wordProcessor = new WordProcessor();
        if (args.length > 0) 
            wordProcessor.setFilePath(args[0]);
        wordProcessor.createAndShowGui();
        wordProcessor.setVisible(true);
    }
}
