
package dictionary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


public class MainPanel extends JPanel {
    public static final int DEFAULT_NUM_COLUMNS = 15;
    private final JButton searchButton, editButton, addButton;
    private final JTextArea textArea;
    private final JTextField searchField;
    private final JComboBox resoultBox;
    private List<Definition> definitionList;
    private final Map<String, Collection<Definition>> map;
    private boolean successfulSearch = false;
    
    /**
     * Constructor.
     * @param dMap - reference to the map with definitions.
     * */
    public MainPanel(Map<String, Collection<Definition>> dMap) {
        setLayout(new BorderLayout());
        JPanel northPanel = new JPanel(new FlowLayout());
        map = dMap;
        searchButton = new JButton("Wyszukaj");
        searchButton.addActionListener(new SearchButtonHandler());
        searchField = new JTextField(DEFAULT_NUM_COLUMNS);
        
        northPanel.add(searchField);
        northPanel.add(searchButton);        
        add (northPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JPanel southPanel = new JPanel(); // footer
        southPanel.add(new JLabel("Witaj w słowniku. Jesteś w panelu głównym."));
        add (southPanel, BorderLayout.SOUTH);
        
        textArea = new JTextArea("Tutaj umieszczane są wyniki wyszukiwania.", DEFAULT_NUM_COLUMNS, DEFAULT_NUM_COLUMNS * 2);
        textArea.setEditable(false);
        final String KEY = "ENTER";
        searchField.getInputMap().put(KeyStroke.getKeyStroke(KEY), KEY);
        searchField.getActionMap().put(KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearchAction();
            }
        });
        
        JPanel innerButtonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Dodaj nowy rekord");
        addButton.addActionListener((ActionEvent e) -> {
            Dictionary.swapPanel("newRecordPanel");
        });
        editButton = new JButton("Edytuj/Usuń");   
        editButton.addActionListener(new EditButtonHandler());
        innerButtonPanel.add(addButton);
        innerButtonPanel.add(editButton);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=gbc.gridy=0;
        
        centerPanel.add(new JScrollPane(textArea), gbc);
        
        resoultBox = new JComboBox();
        resoultBox.setPreferredSize(new Dimension(150*2, 30));
        initSampleDefinitionList();
        initSampleTextArea();
        
        gbc.gridy++;
        centerPanel.add(resoultBox, gbc);
        
        gbc.gridy++;
        centerPanel.add(innerButtonPanel, gbc);       
        
        add (centerPanel, BorderLayout.CENTER);
    }   

    private void initSampleDefinitionList() {
        definitionList = new ArrayList<>();        
        for (int i=0; i<15; i++)
            definitionList.add(new Definition("Phrase "+(i+1), "sample definition text "+(i+1)));        
        resoultBox.setModel(new DefaultComboBoxModel(definitionList.toArray()));
    }

    private void initSampleTextArea() {
        textArea.setText("");
        for (Definition d : definitionList) {
            String p = d.getPhrase();
            String t = d.getDefinitionText();
            textArea.append(p + " - " + t + "\n\n");
        }
    }

    private class EditButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!successfulSearch)
                return;
            
            int index = resoultBox.getSelectedIndex();
            String phrase = definitionList.get(index).getPhrase().toLowerCase().trim();
            EditPanel.setOldKey(phrase, index);
            Dictionary.swapPanel("editPanel");            
        }
    }
    
    private class SearchButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            performSearchAction();
        }        
    }
    /**
     * Method is used for searching. It checks if given phrase exists.
     * */
    private void performSearchAction() {
        String phrase = searchField.getText().trim().toLowerCase();
        //System.out.println("map size: " + map.size());
        Collection <Definition> defs = null;
        defs = map.get(phrase);
        if (defs != null && !defs.isEmpty())
            updateAll(defs);
        else {
            System.out.println("found nothing");
        }
    }
    
    /**
     * If search for the record was successful it updates all visible records
     * @param defs - collection of definitions that have been found.
     * */
    private void updateAll(Collection<Definition> defs) {
        textArea.setText("");
        List<Definition> list = new ArrayList<>();
        for (Definition d : defs) {
            textArea.append(d.getPhrase() + " - " + d.getDefinitionText() + "\n\n");
            list.add(d);
        }
        resoultBox.setModel(new DefaultComboBoxModel(list.toArray()));
        definitionList = list;
        successfulSearch = true;
    }
    
    void clearFields() {
        textArea.setText(""); searchField.setText("");
        successfulSearch = false;
    }
}
