package dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Panel where new records are added.
 * */
@SuppressWarnings("serial")
public class NewRecordPanel extends AbstractPanel {
    private final MainPanel mainPanel;
    
    /**
     * Constructor of the class.
     * @param m - reference to the Map with definitions.
     * @param mp - reference to the instance of MainPanel object.
     * */
    public NewRecordPanel(Map<String, Collection<Definition>> m, MainPanel mp) {
        super(m, "Panel dodawania.");
        mainPanel = mp;
    }
   
    /**
     * Adds new record to the Dictionary. Also updates the data file on HDD.
     */
    @Override
    protected void confirmButtonAction() {
        String t = textArea.getText().trim();
        String p = textField.getText().trim();
        
        if (t.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pola nie mogą być puste!");
            return;
        }
        
        if (t.contains(Dictionary.SEPARATOR)) {
            JOptionPane.showMessageDialog(this,
                    "Znak ';' jest niedozwolony. Wszystkie zostały zastąpione przez '|'");
            t = t.replace(Dictionary.SEPARATOR, " |");
        }
        
        Definition def = new Definition(p, t);
        String key = p.toLowerCase();                
        
        List<Definition> list = null;
        list  = (List<Definition>) map.get(key);
        
        if (list == null || list.isEmpty()) { // new record
            list = new ArrayList<>(2);
            list.add(def);
            map.put(key, list);
        } else { // key exists, add new item to the list
            list.add(def);
        }
        
        try {
            Dictionary.updateFile();
            clearFields();
            mainPanel.clearFields();
            Dictionary.swapPanel("mainPanel");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisu do pliku.\nSpróbuj ponownie.");
        }
    }
}
