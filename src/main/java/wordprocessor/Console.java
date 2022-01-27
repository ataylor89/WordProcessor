package wordprocessor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 *
 * @author andrewtaylor
 */
public class Console extends JTextArea {

    private Process process;
    private String prefix = "%";
    private int prefixPosition;
    private Config config;

    public Console(Config config) {      
        setupKeyStrokes();
        this.prefix = config.getPrefix();
    }

    public void run(String cmd) {
        if (!hasRunningProcess()) {
            String[] args = cmd.split("\\s+");
            try {
                ProcessBuilder pb = new ProcessBuilder(args);
                pb.redirectErrorStream(true);
                process = pb.start();
                startNewLine();
                append(cmd + "\n");
                read(process);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    
    private void read(Process process) {
        Thread thread = new Thread(() -> {
            try {
                BufferedReader reader = process.inputReader();
                String line = reader.readLine();
                while (line != null) {
                    append(line + "\n");
                    line = reader.readLine();
                }
                int exitVal = process.waitFor();
                append("Process exited with value " + exitVal + "\n");
            } catch (IOException | InterruptedException ex) {
                System.err.println(ex);
            } finally { 
                this.process = null;
                startNewLine();
            }
        });
        thread.start();
    }
    
    public boolean hasRunningProcess() {
        return process != null && process.isAlive();
    }
    
    public void closeRunningProcess() {
        if (hasRunningProcess()) { 
            process.destroy(); 
            process = null;
        }
        startNewLine();
    }
    
    private void setupKeyStrokes() {
        InputMap im = getInputMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "ctrl+c");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), "ctrl+d");
        ActionMap am = getActionMap();
        Action keyboardInterrupt = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Closing running process...");
                closeRunningProcess();
            }
        };
        am.put("ctrl+c", keyboardInterrupt);
        am.put("ctrl+d", keyboardInterrupt);
    }
    
    public void setPrefixPosition(int prefixPosition) {
        this.prefixPosition = prefixPosition;
    }
    
    public void setPrefix(String prefix) {
        if (prefix != null && prefix.length() > 0 && prefix.length() < 10)
            this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public void startNewLine() {
        String text = getText();
        if (text.endsWith(prefix + " "))
            return;
        if (!text.isEmpty() && !text.endsWith("\n"))
            append("\n");
        append(prefix + " ");      
    }
    
    public void append(char c) {
        super.append(Character.toString(c));
        setPrefixPosition(getText().length());
        setCaretPosition(getText().length());
    }
    
    @Override
    public void append(String text) {
        super.append(text);
        setPrefixPosition(getText().length());
        setCaretPosition(getText().length());
    }
}
