package dse2pdvoicegrading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Darren Eidson
 */
public class SemesterClass {
    
    private String className;
    
    private int numQuestions;

    private Map<Integer, Question> questionsMap;
    
    private List questionsList;
    
    private List<String> qNames;
    
    public SemesterClass() {
        questionsList = new ArrayList();
        questionsMap = new HashMap<>();
        qNames = new ArrayList();
    }
    
    public SemesterClass(String name) {
        this();
        
        numQuestions = 0;
        
        className = name;
    }
    
    public SemesterClass(JSONObject classJSON) {
        this();
        
        if (classJSON != null) {
            String classNameJSON = (String) classJSON.getOrDefault("name", null);
            JSONArray questionsJSON = (JSONArray) classJSON.getOrDefault("questions", null);

            if (classNameJSON != null) {
                className = classNameJSON;
            } else {
                System.out.println("Error loading semester class: no class name found");
            }

            int numQuestionsJSON = questionsJSON.size();

            if (numQuestionsJSON > 0) {
                for (int i = 0; i < numQuestionsJSON; i++) {
                    JSONObject questionJSON = (JSONObject) questionsJSON.get(i);
                    String text = (String) questionJSON.getOrDefault("text", null);
                    String answer = (String) questionJSON.getOrDefault("answer", null);
                    if (text != null && !text.equals("") && answer != null && !answer.equals("")) {
                        addQuestion(new Question(text, answer, numQuestions));
                    } else {
                        System.out.println("Error loading question: the question and/or answer was missing");
                    }
                }
            }
        } else {
            System.out.println("Error loading semester class: no class object passed");
        }
    }
    
    public int getNewQuestionId() {
        return numQuestions;
    }
    
    public void addQuestion (Question q) {
        int questionId = q.getId();
        
        questionsMap.put(questionId, q);
        questionsList.add(q);
        qNames.add(q.getText());
        
        numQuestions++;
    }
    
    public void updateClassName(String name) {
        this.className = name;
    }

    public String getClassName() {
        return className;
    }

    public Integer getNumQuestions() {
        return questionsMap.size();
    }

    public ObservableList getQuestions() {
        return FXCollections.observableList(questionsList);
    }

    public void deleteAllQuestions() {
        questionsMap = null;
        questionsList = null;
        qNames = null;
    }

    public boolean questionExists(String text) {
        return qNames.contains(text);
    }

    public void deleteQuestion(int id) {
        Question q = questionsMap.get(id);
        
        String text = q.getText();
        
        qNames.remove(text);
        questionsList.remove(q);
        questionsMap.remove(id);
    }

    public void updateQuestion(Question q, String newText, String newAnswer) {
        qNames.remove(q.getText());
        qNames.add(newText);
        
        q.update(newText, newAnswer);
    }
    
    public JSONObject jsonify() {
        JSONObject out = new JSONObject();
        
        JSONArray questions = new JSONArray();
        for (int key : questionsMap.keySet()) {
            if (questionsMap.get(key) != null) {
                questions.add(questionsMap.get(key).jsonify());
            }
        }
        
        out.put("name", className);
        out.put("questions", questions);
        
        return out;
    }

    public Question getQuestion(int id) {
        return questionsMap.get(id);
    }

    public Integer[] getQuestionIds() {
        Set<Integer> keySet = questionsMap.keySet();
        
        return keySet.toArray(new Integer[keySet.size()]);
    }
}
