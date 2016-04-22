package dse2pdvoicegrading;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 *
 * @author Darren Eidson
 */
public class AlertMessage {
    
    public static boolean alertYesNo(AlertTypes type, String message) {
        Alert alert = buildAlert(type);
        alert.setContentText(message);
        
        ButtonType buttonYes = new ButtonType("Yes", ButtonData.YES);
        ButtonType buttonNo = new ButtonType("No", ButtonData.NO);
        
        alert.getButtonTypes().setAll(buttonYes, buttonNo);
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == buttonYes) {
            return true;
        } else {
            return false;
        }
    }

    public static Alert alertError(AlertTypes type, String message) {
        Alert alert = buildAlert(type);
        alert.setContentText(message);
        
        return alert;
    }
    
    public static Alert alertError(AlertTypes type, String message, Exception e) {
        Alert alert = buildAlert(type);
        alert.setContentText(message);
        
        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            
            e.printStackTrace(pw);
            
            String exceptionText = sw.toString();
            
            Label label = new Label("The exception stacktrace was:");
            
            TextArea ta = new TextArea(exceptionText);
            ta.setEditable(false);
            ta.setWrapText(true);
            
            ta.setMaxWidth(Double.MAX_VALUE);
            ta.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow((ta), Priority.ALWAYS);
            GridPane.setHgrow(ta, Priority.ALWAYS);
            
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(ta, 0, 1);
            
            alert.getDialogPane().setExpandableContent(expContent);
        }
        
        return alert;
    }

    private static Alert buildAlert(AlertTypes type) {
        Alert alert = new Alert(type.getType());
        alert.setTitle(type.getTitle());
        alert.setHeaderText(type.getHeader());
        
        return alert;
    }

    public static String alertTextBox(String title, String header, String text) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setContentText(text);
        
        if (!"".equals(header)) {
            dialog.setHeaderText(header);
        } else {
            dialog.setHeaderText(title);
        }
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        } else {
            return "";
        }
    }

    public static String alertEditTextBox(String title, String header, String text, String value) {
        TextInputDialog dialog = new TextInputDialog(value);
        dialog.setTitle(title);
        dialog.setContentText(text);
        
        if (!"".equals(header)) {
            dialog.setHeaderText(header);
        } else {
            dialog.setHeaderText(title);
        }
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        } else {
            return "";
        }
    }

    public static void alertOk(AlertTypes type, String name, String value) {
        String message = "The " + name + " that you tried creating, \"" + value + "\", already exists";
        
        Alert alert = buildAlert(type);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
    }

    public static String alertDropDown(String title, String header, String text) {
        // create list of available choices
        List<String> choices = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        
        // create the dialog with the list as the values
        ChoiceDialog<String> dialog = new ChoiceDialog<>("A", choices);
        dialog.setTitle(title);
        dialog.setContentText(text);
        
        if (!"".equals(header)) {
            dialog.setHeaderText(header);
        } else {
            dialog.setHeaderText(title);
        }
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        } else {
            return "";
        }
    }

    public static String alertEditDropDown(String title, String header, String text, String value) {
        // create list of available choices
        List<String> choices = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        
        // create the dialog with the list as the values
        ChoiceDialog<String> dialog = new ChoiceDialog<>(value, choices);
        dialog.setTitle(title);
        dialog.setContentText(text);
        
        if (!"".equals(header)) {
            dialog.setHeaderText(header);
        } else {
            dialog.setHeaderText(title);
        }
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        } else {
            return "";
        }
    }

    public static AlertMessageResult alertSaveFirstConfirmation() {
        Alert alert = buildAlert(AlertTypes.SAVE_FIRST_CONFIRMATION);
        alert.setContentText("Do you want to save the changes first?");
        
        ButtonType buttonYes = new ButtonType("Yes", ButtonData.YES);
        ButtonType buttonNo = new ButtonType("No", ButtonData.NO);
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.OTHER);
        
        alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == buttonYes) {
            return AlertMessageResult.YES;
        } else if (result.get() == buttonCancel) {
            return AlertMessageResult.CANCEL;
        } else {
            return AlertMessageResult.NO;
        }
    }
}
