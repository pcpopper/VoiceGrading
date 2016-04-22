package settings;

import dse2pdvoicegrading.DocumentFunctions;
import dse2pdvoicegrading.Dse2pdVoiceGrading;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Darren Eidson
 */
public class UserSettings {
    
    final private String filename = "userSettings.xml";
    
    Map<String, Settings> userSettings;
    
    public UserSettings() {
        loadSettings();
    }
    
    public Settings getSetting(String settingName) {
        return userSettings.get(settingName);
    }
    
    private void loadSettings() {
        userSettings = new HashMap<>();
        
        try {
            Document doc = DocumentFunctions.readDocument(getClass().getResourceAsStream(filename));
            
            NodeList rfList = doc.getElementsByTagName("recentFiles");
            if (rfList.getLength() > 0) {
                RecentFiles recentFiles = new RecentFiles();
                for (int i = 0; i < rfList.getLength(); i++) {
                    Node file = rfList.item(i);
                    
                    if (file.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) file;
                        recentFiles.addFile(
                                e.getElementsByTagName("name").item(0).getTextContent(), 
                                e.getElementsByTagName("filename").item(0).getTextContent()
                        );
                        
                        if (i == 0) {
                            userSettings.put("RecentFile", new StringSetting(e.getElementsByTagName("name").item(0).getTextContent()));
                            userSettings.put("RecentFilename", new StringSetting(e.getElementsByTagName("filename").item(0).getTextContent()));
                        }
                    }
                }
                userSettings.put("RecentFiles", recentFiles);
            }
        } catch (Exception e) {
            Dse2pdVoiceGrading.errorMessage(e);
        }
    }
    
//    private void saveSettings() {
//        File f = new File(filename);
//        if(f.exists() && !f.isDirectory()) { 
//            System.out.println("exists");
//        } else {
//            System.out.println("does not exists");
//        }
//    }

    public boolean isSet(String name) {
        return (userSettings.get(name) != null);
    }
}
