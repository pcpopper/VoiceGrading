package dse2pdvoicegrading;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import scenes.Switchable;
import settings.UserSettings;

/**
 *
 * @author Darren Eidson
 */
public class Dse2pdVoiceGrading extends Application {
    
    public static UserSettings userSettings;
    
    public static Semester currentSemester;
    public static SemesterClass currentClass;
    
    public static Stage stage;
    
    /**
     * Used to enable/disable debugging error alerts.
     * <p>
     * Set to true to enable alert dialogs, false to show exceptions in debugger.
     */
    public static final boolean debug = true;
    /**
     * Used to enable/disable stacktrace printouts.
     * <p>
     * Set to true to enable stacktrace printouts in the dialogs/debugger.
     */
    public static final boolean stackTrace = true;
    
    /**
     * The starting function for the application.
     * <p>
     * This function creates and calls Switchable and UserSettings
     * 
     * @param stage Stage The stage that is passed in by JavaFX
     * @throws Exception 
     * @see UserSettings
     * @see Switchable
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        userSettings = new UserSettings();
        
        // UI if SceneManager can't switch to a Scene
        HBox root = new HBox();
        root.setPrefSize(600, 400);
        root.setAlignment(Pos.CENTER);
        Text message = new Text("There was an error, try restarting this application.");
        message.setFont(Font.font(STYLESHEET_MODENA, 32));
        root.getChildren().add(message);
        
        // create Scene and init UI to show failure in case switch fails
        Scene scene = new Scene(root);
        
        Switchable.scene = scene;
        Switchable.switchTo("MainScene");
        
//        Screen screen = Screen.getPrimary();
//        Rectangle2D bounds = screen.getVisualBounds();

//        stage.setX(bounds.getMinX());
//        stage.setY(bounds.getMinY());
//        stage.setWidth(bounds.getWidth());
//        stage.setHeight(bounds.getHeight());

        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Function that displays the error dialog box.
     * 
     * @param e Exception The exception that was thrown by the calling method.
     */
    public static void errorMessage(Exception e) {
        getErrorMessage(e.getMessage(), e).showAndWait();
    }
    /**
     * Function that returns the error dialog box for the caller to use.
     * 
     * @param e Exception The exception that was thrown by the calling method.
     * @return Alert The generated alert created by getErrorMessage().
     * @see #getErrorMessage(java.lang.String, java.lang.Exception)
     */
    public static Alert alertErrorMessage(Exception e) {
        return getErrorMessage(e.getMessage(), e);
    }
    /**
     * Creates an error message and/or stacktrace depending on the settings of debug and stacktrace.
     * 
     * @param message String The message of the error, can be different from the message provided in the exception.
     * @param e Exception The exception that was thrown by the caller.
     * @return Alert Will return null if debug is false, otherwise will return the alert dialog.
     * @see #debug
     * @see #stackTrace
     */
    private static Alert getErrorMessage(String message, Exception e) {
        Alert alert = null;
        if (debug) {
            if (stackTrace) {
                alert = AlertMessage.alertError(AlertTypes.EXCEPTION, message, e);
            } else {
                alert = AlertMessage.alertError(AlertTypes.EXCEPTION, message);
            }
        } else {
            if (stackTrace) {
                System.out.println(message);
                e.printStackTrace();
            } else {
                System.out.println(message);
            }
        }
        
        return alert;
    }
    
    /**
     * The main class for the Java application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
