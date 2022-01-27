package wordprocessor;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author andrewtaylor
 */
public class JavaForm extends JPanel {

    private GridBagLayout gridbag;
    private JTextField classpath, args;

    public JavaForm() {
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
