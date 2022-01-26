package wordprocessor;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.text.BadLocationException;

/**
 *
 * @author andrewtaylor
 */
public class MenuBar extends JMenuBar implements MenuListener, ActionListener {
    
    private WordProcessor wp;
    private JMenu file;
    private JMenuItem newFile, save, saveAs, openFile, openURL, exit;
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
    private JFileChooser fileChooser;
    
    public MenuBar(WordProcessor wp) {
        super();
        this.wp = wp;
        addComponents();
    }
    
    private void addComponents() {
        file = new JMenu("File");
        file.addMenuListener(this);
        newFile = new JMenuItem("New file");
        newFile.addActionListener(this);
        save = new JMenuItem("Save");
        save.addActionListener(this);
        saveAs = new JMenuItem("Save as");
        saveAs.addActionListener(this);
        openFile = new JMenuItem("Open file");
        openFile.addActionListener(this);
        openURL = new JMenuItem("Open URL");
        openURL.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        file.add(newFile);
        file.add(save);
        file.add(saveAs);
        file.add(openFile);
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
        add(file);
        add(colors);
        add(theme);
        add(tools);
        add(email);
        add(run);
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
    
    private String getFilenameWithoutExtension() {
        String filename = currentFile.getName();
        int lio = filename.lastIndexOf(".");
        if (lio > 0) {
            filename = filename.substring(0, lio);
        }
        return filename;
    }

    private String getPathWithoutExtension() {
        String path = currentFile.getPath();
        int lio = path.lastIndexOf(".");
        if (lio > 0) {
            path = path.substring(0, lio);
        }
        return path;
    }

    private void enableDisableMenuItems() {
        save.setEnabled(wp.getFile() != null);
        sendEmail.setEnabled(wp.getFile() != null);
        runJavaProgram.setEnabled(wp.getFile() != null);
        runPythonProgram.setEnabled(wp.getFile() != null);
    }
    
    @Override
    public void menuSelected(MenuEvent e) {
        enableDisableMenuItems();
    }

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFile) {
            promptForSave();
            newFile();
        } else if (e.getSource() == save) {
            saveToFile();
        } else if (e.getSource() == saveAs) {
            saveToFileAs();
        } else if (e.getSource() == open) {
            promptForSave();
            openFile();
        } else if (e.getSource() == downloadWebPage) {
            promptForSave();
            newFile();
            String address = JOptionPane.showInputDialog(this, "URL:", "Download web page", JOptionPane.QUESTION_MESSAGE);
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
            WordProcessor.EmailPanel panel = new WordProcessor.EmailPanel();
            String[] options = new String[]{"Cancel", "Send"};
            int value = JOptionPane.showOptionDialog(this, panel, "Compose an email", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (value == 0) {
                return;
            }
            email(panel.getTo(), panel.getFrom(), panel.getSubject(), textArea.getText(), panel.getFrom(), panel.getPassword());
        } else if (e.getSource() == compileJavaProgram) {
            String classpath = JOptionPane.showInputDialog(this, "Classpath:", "Classpath", JOptionPane.QUESTION_MESSAGE);
            String cmd = "javac ";
            if (classpath != null && classpath.length() > 0) {
                cmd += "-classpath " + classpath + " ";
            }
            cmd += currentFile.getPath();
            WordProcessor.ProcessController process = new WordProcessor.ProcessController(this, "Compiling Java program...", cmd);
            process.start();
        } else if (e.getSource() == compileCProgram) {
            String cmd = "gcc " + currentFile.getPath() + " -o " + getPathWithoutExtension();
            WordProcessor.ProcessController process = new WordProcessor.ProcessController(this, "Compiling C program with gcc...", cmd);
            process.start();
        } else if (e.getSource() == compileCPPProgram) {
            String cmd = "g++ -c " + currentFile.getPath() + " -o " + getPathWithoutExtension() + ".o";
            WordProcessor.ProcessController process = new WordProcessor.ProcessController(this, "Compiling C++ program with g++", cmd);
            process.start();
        } else if (e.getSource() == compileNASMProgram) {
            String cmd = "nasm -fmacho64 " + currentFile.getPath();
            WordProcessor.ProcessController process = new WordProcessor.ProcessController(this, "Compiling NASM program...", cmd);
            process.start();
        } else if (e.getSource() == linkObjectCode) {
            String sharedLibraries = JOptionPane.showInputDialog(this, "Shared libraries:", "Shared libraries", JOptionPane.QUESTION_MESSAGE);
            String cmd = "ld -macosx_version_min 10.7 ";
            if (sharedLibraries != null && sharedLibraries.length() > 0) {
                cmd += sharedLibraries + " ";
            }
            cmd += getPathWithoutExtension() + ".o -o " + getPathWithoutExtension();
            WordProcessor.ProcessController process = new WordProcessor.ProcessController(this, "Linking object code with ld...", cmd);
            process.start();
        } else if (e.getSource() == runJavaProgram) {
            WordProcessor.JavaPanel panel = new WordProcessor.JavaPanel();
            String[] options = new String[]{"Cancel", "OK"};
            int value = JOptionPane.showOptionDialog(this, panel, "Classpath and Args", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (value == 0) {
                return;
            }
            String classpath = panel.getClasspath();
            String args = panel.getArgs();
            if (classpath == null || classpath.length() == 0) {
                classpath = currentFile.getParent();
            }
            String classname = getFilenameWithoutExtension();
            String cmd = "java -classpath " + classpath + " " + classname;
            if (args != null & args.length() > 0) {
                cmd += " " + args;
            }
            WordProcessor.ProcessController process = new WordProcessor.ProcessController(this, "Running Java program...", cmd);
            process.start();
        } else if (e.getSource() == runPythonProgram) {
	    String args = JOptionPane.showInputDialog(this, "Args:", "Runtime arguments", JOptionPane.QUESTION_MESSAGE);
            String cmd = "python " + currentFile.getPath();
	    if (args != null && args.length() > 0)
	        cmd += " " + args;
            WordProcessor.ProcessController process = new WordProcessor.ProcessController(this, "Running Python program...", cmd);
            process.start();
        } else if (e.getSource() == runMachineCodeProgram) {
	    String args = JOptionPane.showInputDialog(this, "Args:", "Runtime arguments", JOptionPane.QUESTION_MESSAGE);
            String cmd = getPathWithoutExtension();
	    if (args != null && args.length() > 0)
		cmd += " " + args;
            WordProcessor.ProcessController process = new WordProcessor.ProcessController(this, "Running machine code program...", cmd);
            process.start();
        }
    }
}
