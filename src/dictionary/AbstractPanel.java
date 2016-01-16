
package dictionary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public abstract class AbstractPanel extends JPanel {
    protected Map<String, Collection<Definition>> map;
    private final JButton backButton, confirmButton;
    protected JTextField textField;
    protected JTextArea textArea;
    
    /**
     * Constructor.
     * @param m - reference to the map of values.
     * @param title - Text that will be shown on the bottom of the panel.
     * */
    public AbstractPanel(Map<String, Collection<Definition>> m, String title) {
        super(new BorderLayout()); // set layout
        map = m;
        JPanel southPanel = new JPanel(); // footer
        southPanel.add(new JLabel(title));
        add(southPanel, BorderLayout.SOUTH);

        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.add(new JLabel("Fraza wyszukiwania: "));
        textField = new JTextField(MainPanel.DEFAULT_NUM_COLUMNS);
        northPanel.add(textField);
        add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        textArea = new JTextArea("Nowa definicja...",
                MainPanel.DEFAULT_NUM_COLUMNS, MainPanel.DEFAULT_NUM_COLUMNS * 2);
        centerPanel.add(new JScrollPane(textArea), gbc);

        JPanel innerButtonPanel = new JPanel(new FlowLayout());
        confirmButton = new JButton("Zatwierdź");
        confirmButton.addActionListener((ActionEvent e) -> {
            confirmButtonAction();
        });
        backButton = new JButton("Powrót");
        backButton.addActionListener((ActionEvent e) -> {
            Dictionary.swapPanel("mainPanel");
        });

        innerButtonPanel.add(confirmButton);
        innerButtonPanel.add(backButton);

        gbc.gridy++;
        centerPanel.add(innerButtonPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * This method must be override to provide the action of the button. 
     */
    protected abstract void confirmButtonAction();
    
    /**
     * Clears the text fields. Setting current text to "".
     */
    protected final void clearFields() {
        textArea.setText(""); textField.setText("");
    }
}
