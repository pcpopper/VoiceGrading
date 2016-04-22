package dse2pdvoicegrading;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Set;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;

/**
 *
 * @author Darren Eidson
 */
public class DocumentFunctions {
    
    public static Document readDocument(InputStream filename) {
        Document doc = null;
        
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(filename);

            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            Dse2pdVoiceGrading.errorMessage(e);
        }
        
        return doc;
    }
    
    public static JSONObject readJSON(String filename) {
        try {
            FileReader reader = new FileReader(filename);
            
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            return jsonObject;
        } catch (Exception e) {
            Dse2pdVoiceGrading.errorMessage(e);
            return null;
        }
    }
    
    public static boolean writeJSON(String filename, Semester currentSemester) {
        JSONObject obj = new JSONObject();
        obj.put("semester", currentSemester.jsonify());
        
        try (FileWriter file = new FileWriter(filename);) {
            file.write(obj.toJSONString());
            
            return true;
        } catch (Exception e) {
            Dse2pdVoiceGrading.errorMessage(e);
            return false;
        }
    }
    
    public static File saveDialog(Stage stage, String semesterName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Semester");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(semesterName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        
        return fileChooser.showSaveDialog(stage);
    }

    public static File openDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Semester");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        
        return fileChooser.showOpenDialog(stage);
    }
}
