package wordprocessor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
import java.util.Properties;
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
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.BadLocationException;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class WordProcessor extends JFrame implements ActionListener {

    private JMenuBar bar;
    private JMenu file;
    private JMenuItem newFile, save, saveAs, open, openURL, exit;
    private JMenu colors;
    private JMenuItem fgcolor, bgcolor, whiteblack, whitegray, grayblue, tealwhite, purplewhite, seaTheme;
    private JMenu tools;
    private JMenuItem lineCount, characterCount, gotoLine, copyToClipboard;
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
    private JDialog consoleDialog;
    private Console console;
    private EmailForm emailForm;
    private Config config;
    private Logger logger;
    
    public WordProcessor() {
        super("Word Processor");
        logger = AppLogger.getLogger();
        config = new Config();
    }
    
    public void createAndShowGui() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bar = new JMenuBar();
        file = new JMenu("File");
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
        tools = new JMenu("Tools");
        lineCount = new JMenuItem("Check line count");
        lineCount.addActionListener(this);
        characterCount = new JMenuItem("Check character count");
        characterCount.addActionListener(this);
        gotoLine = new JMenuItem("Goto line number");
        gotoLine.addActionListener(this);
        copyToClipboard = new JMenuItem("Copy text to clipboard");
        copyToClipboard.addActionListener(this);
        tools.add(lineCount);
        tools.add(characterCount);
        tools.add(gotoLine);
        tools.add(copyToClipboard);
        email = new JMenu("Email");
        sendEmail = new JMenuItem("Send email");
        sendEmail.addActionListener(this);
        email.add(sendEmail);
        run = new JMenu("Run");
        runJavaProgram = new JMenuItem("Run Java program");
        runJavaProgram.addActionListener(this);
        runPythonProgram = new JMenuItem("Run Python program");
        runPythonProgram.addActionListener(this);
        run.add(runJavaProgram);
        run.add(runPythonProgram);
        bar.add(file);
        bar.add(colors);
        bar.add(tools);
        bar.add(email);
        bar.add(run);
        setJMenuBar(bar);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        add(panel);
        fileChooser = new JFileChooser();
        console = new Console();
        JScrollPane sp = new JScrollPane(console);
        consoleDialog = new JDialog(this, "Console");
        consoleDialog.setSize(600, 500);
        consoleDialog.add(sp);
        emailForm = new EmailForm();
    }
    
    public void loadSettings() {
        logger.info("Loading settings");
        config.loadConfig();
        String prefix = config.getPrefix();
        foregroundColor = config.getForegroundColor();
        backgroundColor = config.getBackgroundColor();
        refreshColors();
        console.setPrefix(prefix);
    }

    public void setupKeyStrokes() {
	InputMap im = (InputMap) UIManager.get("TextArea.focusInputMap");
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
    }

    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            logger.warning(e.toString());
        }
    }

    private boolean isSaved() {
        try {
            String fileContents = Files.readString(currentFile.toPath());
            if (textArea.getText().equals(fileContents))
                return true;
        } catch (IOException e) {
            logger.warning(e.toString());
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
        refreshMenuItems();
    }

    private void saveToFile(File file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            String text = textArea.getText();
            bufferedWriter.write(text);
            refreshMenuItems();
        } catch (IOException ex) {
            logger.warning(ex.toString());
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
            refreshMenuItems();
        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }

    private void openFile() {
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            openFile(f);
        }
    }
    
    private void openURL() {
        String address = JOptionPane.showInputDialog(this, "URL:", "Open URL", JOptionPane.QUESTION_MESSAGE);
        openURL(address);
    }
    
    private void openURL(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) connection.getContent()));
            String text = br.lines().collect(Collectors.joining("\n"));
            textArea.setText(text);
            refreshMenuItems();
            currentFile = null;
        } catch (MalformedURLException ex) {
            logger.warning(ex.toString());
        } catch (IOException ex) {
            logger.warning(ex.toString());
        }
    }

    private void sendEmail() {
        String[] options = new String[]{"Cancel", "Send"};
        int option = JOptionPane.showOptionDialog(null, emailForm, "Compose an email", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (option == 1)
            sendEmail(emailForm.getTo(), 
                    emailForm.getFrom(), 
                    emailForm.getSubject(), 
                    textArea.getText(), 
                    emailForm.getFrom(), 
                    emailForm.getPassword());
        emailForm.clear();
    }
    
    private void sendEmail(String to, String from, String subject, String body, String username, String password) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
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
            logger.warning(e.toString());
        } catch (MessagingException e) {
            logger.warning(e.toString());
        }
    }
    
    private void refreshMenuItems() {
        save.setEnabled(currentFile != null);
        sendEmail.setEnabled(currentFile != null);
        runJavaProgram.setEnabled(currentFile != null);
        runPythonProgram.setEnabled(currentFile != null);
    }

    private void refreshColors() {
        textArea.setForeground(foregroundColor);
        textArea.setBackground(backgroundColor);
    }
    
    private void promptForLineNumber() {
        int lineNumber = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter a line number:", "Goto line", JOptionPane.QUESTION_MESSAGE));
        try {
            textArea.setCaretPosition(textArea.getLineStartOffset(lineNumber));
        } catch (BadLocationException ex) {
            logger.warning(ex.toString());
            textArea.setCaretPosition(textArea.getText().length());
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
        } else if (e.getSource() == openURL) {
            promptForSave();
            openURL();
        } else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } else if (e.getSource() == fgcolor) {
            Color color = JColorChooser.showDialog(this, "Select a foreground color", foregroundColor);
            if (color != null) 
                foregroundColor = color;
            textArea.setForeground(foregroundColor);
        } else if (e.getSource() == bgcolor) {
            Color color = JColorChooser.showDialog(this, "Select a background color", backgroundColor);
            if (color != null) 
                backgroundColor = color;
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
        } else if (e.getSource() == lineCount) {
            JOptionPane.showMessageDialog(this, "There are " + textArea.getLineCount() + " lines in the file");
        } else if (e.getSource() == characterCount) {
            JOptionPane.showMessageDialog(this, "There are " + textArea.getText().length() + " characters in the file");
        } else if (e.getSource() == gotoLine) {
            promptForLineNumber();
        } else if (e.getSource() == copyToClipboard) {
            StringSelection stringSelection = new StringSelection(textArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } else if (e.getSource() == sendEmail) {
            sendEmail();
        } else if (e.getSource() == runJavaProgram) {
            String cmd = "java " + currentFile.getPath();
            consoleDialog.setVisible(true);
            console.run(cmd);
        } else if (e.getSource() == runPythonProgram) {
	    String cmd = "python " + currentFile.getPath();
            consoleDialog.setVisible(true);
            console.run(cmd);           
        } 
    }
    
    public static void main(String[] args) {
        WordProcessor wordProcessor = new WordProcessor();
        wordProcessor.setLookAndFeel();
        wordProcessor.setupKeyStrokes();
        wordProcessor.createAndShowGui();
        wordProcessor.loadSettings();
        wordProcessor.setVisible(true);
        if (args.length > 0)
            wordProcessor.openFile(new File(args[0]));
    }
}