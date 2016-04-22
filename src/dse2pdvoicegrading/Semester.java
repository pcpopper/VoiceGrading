package dse2pdvoicegrading;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Darren Eidson
 */
public class Semester {
    
    private Map<String, SemesterClass> classes;
    
    private Boolean isLoaded = false;
    
    private String semesterName;
    
    public Semester(String semesterName) {
        classes = new HashMap<>();
        
        this.semesterName = semesterName;
    }
    
    public Semester(String filename, String semesterName) {
        this(semesterName);
        
        try {
            JSONObject document = DocumentFunctions.readJSON(filename);

            if (document != null) {
                JSONArray semesterJSON = (JSONArray) document.getOrDefault("semester", null);

                if (semesterJSON != null) {
                    int numClasses = semesterJSON.size();

                    if (numClasses > 0) {
                        for (int i = 0; i < numClasses; i++) {
                            JSONObject classJSON = (JSONObject) semesterJSON.get(i);
                            String name = (String) classJSON.getOrDefault("name", null);
                            if (name != null && !name.equals("")) {
                                addClass(new SemesterClass(classJSON));
                            }
                        }
                    }

                    isLoaded = true;
                } else {
                    throw new Exception("Error loading the semester: the file has become corrupted");
                }
            } else {
                throw new IOException("Error loading the semester: the file was empty");
            }
        } catch (Exception e) {
            Dse2pdVoiceGrading.errorMessage(e);
        }
    }
    
    public Boolean semesterExists() {
        return isLoaded;
    }
    
    public String getName() {
        return semesterName;
    }
    
    public void addClass(SemesterClass sClass) {
        String name = sClass.getClassName();
        classes.put(name, sClass);
    }

    public ObservableList getClasses() {
        ObservableList out = FXCollections.observableArrayList();
        
        for (String key : classes.keySet()) {
            out.add(key);
        }
       
        return out;
    }
    
    public void updateClassName(SemesterClass currentClass, String newName) {
        String oldName = currentClass.getClassName();
        
        currentClass.updateClassName(newName);
        
        classes.put(newName, currentClass);
        classes.remove(oldName);
    }

    public SemesterClass getClass(String name) {
        return classes.get(name);
    }

    public boolean classExists(String name) {
        return (classes.get(name) != null);
    }
    
    public void deleteClass(String className) {
        classes.remove(className);
    }
    
    public void deleteAllClasses() {
        for (String key : classes.keySet()) {
            if (classes.get(key) != null) {
                classes.get(key).deleteAllQuestions();
            }
        }
        
        classes = null;
    }
    
    public JSONArray jsonify() {
        JSONArray out = new JSONArray();
        
        for (String key : classes.keySet()) {
            if (classes.get(key) != null) {
                out.add(classes.get(key).jsonify());
//                System.out.println(classes.get(key).getClassName());
            }
        }
        
        return out;
    }
}
