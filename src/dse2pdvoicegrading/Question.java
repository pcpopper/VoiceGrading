package dse2pdvoicegrading;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.json.simple.JSONObject;

/**
 *
 * @author Darren Eidson
 */
public class Question {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty text = new SimpleStringProperty("");
    private final SimpleStringProperty answer = new SimpleStringProperty("");
    
    private int questionId;
    
    public Question(String text, String answer, int id) {
        if (text != null  && !text.equals("") && answer != null && !answer.equals("") && id >= 0 && id == (int) id) {
            setId(id);
            setText(text);
            setAnswer(answer);
        } else {
            System.out.println("Error adding question: the question text, question id, and/or answer was missing");
        }
    }
    
    public void setId(int id) {
        this.id.set(id + 1);
    }
    
    public void setText(String text) {
        this.text.set(text);
    }
    
    public void setAnswer(String answer) {
        this.answer.set(answer);
    }
    
    public int getId() {
        return (int) id.get();
    }
    
    public String getText() {
        return text.get();
    }
    
    public String getAnswer() {
        return answer.get();
    }
    
    public void update(String text, String answer) {
        setText(text);
        setAnswer(answer);
    }
    
    public JSONObject jsonify() {
        JSONObject out = new JSONObject();
        
        out.put("text", getText());
        out.put("answer", getAnswer());
        
        return out;
    }
}
