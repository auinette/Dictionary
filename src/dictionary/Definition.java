
package dictionary;


public class Definition {
    private String phrase;
    private String text;
    
    /**
     * Constructor.
     * @param p - Phrase. Name of the dictionary entry.
     * @param t - Text that describes the given phrase.
     * */
    public Definition (String p, String t) {
        text = t; phrase = p;
    }
    
    public String getPhrase() {
        return phrase;
    }
    public String getDefinitionText() {
        return text;
    }
    public void setNewDefinitionText(String t) {
        text = t;
    }
    public void setNewPhrase(String p) {
        phrase = p;
    } 
    
    @Override
    public String toString() {
        return phrase + Dictionary.SEPARATOR + text;
    }
}
