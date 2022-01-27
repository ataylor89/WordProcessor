package wordprocessor;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author andrewtaylor
 */
public class EmailForm extends JPanel {

    private GridBagLayout gridbag;
    private JTextField from, to, subject;
    private JPasswordField password;
    private Config config;

    public EmailForm(Config config) {
        this.config = config;
        gridbag = new GridBagLayout();
        super.setLayout(gridbag);
        JLabel fromLabel = new JLabel("From: ");
        from = new JTextField();
        from.setText(config.getEmailSender());
        JLabel toLabel = new JLabel("To: ");
        to = new JTextField();
        to.setText(config.getEmailRecipient());
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
    
    public void reset() {
        from.setText(config.getEmailSender());
        to.setText(config.getEmailRecipient());
        subject.setText("");
        password.setText("");
    }
}
