package settings;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Darren Eidson
 */
public class RecentFiles extends Settings<String[]> {

    private String[] filenames;
    private Map<String, String> files;
    private int index = 0;
    
    public RecentFiles() {
        settingValue = new String[5];
        files = new HashMap<>();
        filenames = new String[5];
    }
    
    public RecentFiles(String[] names, String[] filenames) {
        super(names);
    }
    
    public void addFile(String name, String filename) {
        settingValue[index] = name;
        filenames[index] = filename;
        files.put(name, filename);
        index++;
    }
    
//    public String[] getFilenames() {
//        return {""};
//    }

    @Override
    public String[] getValue() {
        return settingValue;
    }

    @Override
    public void setValue(String[] value) {
        settingValue = value;
    }
    
    public String getFilename(String file) {
        return files.get(file);
    }
}
