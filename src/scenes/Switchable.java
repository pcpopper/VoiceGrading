package scenes;

import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author dale
 */
public abstract class Switchable {
    public static Scene scene;
    public static final HashMap<String, Switchable> controllers = new HashMap<>();
    
    private Parent root;  
    
    public void setRoot(Parent root) {
        this.root = root;
    }
    
    public Parent getRoot() {
        return root;
    }

    public static Switchable add(String name) {
        Switchable controller;
        
        controller = controllers.get(name);
        
        if (controller == null) {
            try {
                FXMLLoader loader = new FXMLLoader(Switchable.class.getResource(name + ".fxml"));
                Parent root = (Parent)loader.load();
                controller = (Switchable)loader.getController();
                controller.setRoot(root);
                controllers.put(name, controller);
            } catch (Exception ex) {
                controller = null;
                System.out.println(ex.toString());
            }
        }
        
        return controller;
    }
    
    public static void switchTo(String name) {
        Switchable controller = controllers.get(name);
        
        if (controller == null) {
            controller = add(name);
        }
        
        if (controller != null) {
            if (scene != null) {
                scene.setRoot(controller.getRoot());
            }
        }
    }
    
    public static Switchable getControllerByName(String name) {
        return controllers.get(name);
    }
}
