package dse2pdvoicegrading;

import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Darren Eidson
 */
public enum AlertTypes {
    DELETE_CONFIRMATION (AlertType.CONFIRMATION, "Deletion Confirmation", "Deletion Confirmation"),
    ALREADY_EXISTS (AlertType.INFORMATION, "File Exists", "File already Exists"),
    SAVE_FIRST_CONFIRMATION (AlertType.CONFIRMATION, "Save First Confirmation", "Save First Confirmation"),
    EXCEPTION (AlertType.ERROR, "Exception Dialog", "There was an error!");
    
    private final AlertType type;
    private final String title;
    private final String header;
    
    AlertTypes(AlertType type, String title, String header) {
        this.type = type;
        this.title = title;
        this.header = header;
    }
    
    public AlertType getType() {
        return type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getHeader() {
        return header;
    }
}
