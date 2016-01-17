
package dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Panel when editions of existing phrases are done.
 * */
@SuppressWarnings("serial")
public class EditPanel extends AbstractPanel {    
    private static String oldKey = null;
    private static int index;
    private final MainPanel mainPanel;
    
    /**
     * Constructor of the class.
     * @param m - reference to the Map with definitions.
     * @param mp - reference to the instance of MainPanel object.
     * */
    public EditPanel(Map<String, Collection<Definition>> m, MainPanel mp) {
        super(m, "Panel edytowania.");
        mainPanel = mp;
    }
    
    /**
     * Edits existing record in the Dictionary. Also updates the data file on HDD.
     */
    @Override
    protected void confirmButtonAction() {
        String newPhrase = textField.getText().trim();
        String newText = textArea.getText().trim();        
        if (newPhrase.isEmpty() || newText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie pola!");
            return;
        }
        
        if (newText.contains(Dictionary.SEPARATOR)) {
            newText = newText.replace(Dictionary.SEPARATOR, " |");
            JOptionPane.showMessageDialog(this,
                    "Znak ';' jest niedozwolony. Wszystkie zostały zastąpione przez '|'");
        System.out.println(newText);
        }
        
        List<Definition> items = (List<Definition>) map.get(oldKey);
        
        if (!newPhrase.toLowerCase().equals(oldKey)) { // new unique key
            Collection<Definition> coll = null;
            coll = map.get(newPhrase.toLowerCase());
            if (coll != null && !coll.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ten klucz jest już używany!");
                return;                
            }
            items.remove(index);   
            List<Definition> tmpList = new ArrayList<>();
            tmpList.add(new Definition(newPhrase, newText));
            map.put(newPhrase.toLowerCase(), tmpList);
            if (items.isEmpty()) 
                map.remove(oldKey);
            
        } else { // add to old key collection
            items.set(index, new Definition(newPhrase.toLowerCase(), newText));
        }
        try {
            Dictionary.updateFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        clearFields();
        mainPanel.clearFields();
        Dictionary.swapPanel("mainPanel");
    }
    
    /**
     * Sets the value of old key and index, that will be modified in EditPanel
     * @param k - vale of the old key.
     * @param i - index of the key (there might be more results than one).
     * */
    static void setOldKey (String k, int i) {
        oldKey = k; index = i;
    }

    
}
