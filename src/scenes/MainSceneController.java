package scenes;

import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import dse2pdvoicegrading.Actions;
import dse2pdvoicegrading.AlertMessage;
import dse2pdvoicegrading.AlertMessageResult;
import dse2pdvoicegrading.AlertTypes;
import dse2pdvoicegrading.DocumentFunctions;
import dse2pdvoicegrading.Dse2pdVoiceGrading;
import dse2pdvoicegrading.Question;
import dse2pdvoicegrading.Semester;
import dse2pdvoicegrading.SemesterClass;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import settings.RecentFiles;
import settings.UserSettings;

/**
 * FXML Controller class
 *
 * @author Darren Eidson
 */
public class MainSceneController extends Switchable implements Initializable {

    private UserSettings userSettings;
    
    private Semester currentSemester;
    private SemesterClass currentClass;
    private Question currentQuestion;
    
    private Boolean semesterHasChanged = false;
    
    private ArrayList<MenuItem> rfMenuItems;
    
    private RecentFiles recentFiles;
    
    private Stage stage;
    
    @FXML
    VBox root;
    
    @FXML
    Button qDelete;
    @FXML
    Button qEdit;
    @FXML
    Button cDelete;
    @FXML
    Button cEdit;
    @FXML
    Button cGrade;
    @FXML
    Button newClass;
    
    @FXML
    Label className;
    @FXML
    Label numQuestions;
    
    @FXML
    ListView classList;
    
    @FXML
    TableView questionList;
    
    @FXML
    TabPane classTabs;
    
    @FXML
    Tab infoTab;
    @FXML
    Tab questionTab;
    
    @FXML
    Menu menu;
    
    @FXML
    TableColumn qId;
    @FXML
    TableColumn question;
    @FXML
    TableColumn answer;
    
    @FXML
    MenuItem saveMenuItem;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // load the user's settings
        userSettings = Dse2pdVoiceGrading.userSettings;
        
        if (Dse2pdVoiceGrading.currentClass != null) {
            currentClass = Dse2pdVoiceGrading.currentClass;
            
            tabInfo();
        } else {
            Question q1 = new Question("test 1", "A", 0);
            Question q2 = new Question("test 2", "B", 1);
            Question q3 = new Question("test 3", "C", 2);
            Question q4 = new Question("test 4", "D", 3);
            Question q5 = new Question("test 5", "E", 4);

            currentClass = new SemesterClass("test");

            currentClass.addQuestion(q1);
            currentClass.addQuestion(q2);
            currentClass.addQuestion(q3);
            currentClass.addQuestion(q4);
            currentClass.addQuestion(q5);
            
            tabInfo();
        }
        if (Dse2pdVoiceGrading.currentSemester != null) {
            currentSemester = Dse2pdVoiceGrading.currentSemester;
            
            populateClassList();
            newClass.setDisable(false);
        } else {
            currentSemester = new Semester("test");
            currentSemester.addClass(currentClass);
            newClass.setDisable(false);

            populateClassList();
        }

        // get the list of recent files
//        recentFiles = (RecentFiles) Dse2pdVoiceGrading.userSettings.getSetting("RecentFiles");
        
        // create the menus
//        buildRecentFilesMenu();
//        if (rfMenuItems != null) {
//            menu.getItems().addAll(rfMenuItems);
//            menu.setDisable(false);
//        }
        
        // if there is a previous file, reload it
//        if (userSettings.isSet("RecentFile")) {
//            loadClassList(
//                    (String) userSettings.getSetting("RecentFilename").getValue(),
//                    (String) userSettings.getSetting("RecentFile").getValue()
//            );
//        }
        
        Dse2pdVoiceGrading.stage.setOnCloseRequest(this::exitApplication);
        
//        stage = (Stage)root.getScene().getWindow();
    }
    
    @FXML
    public void exitApplication(WindowEvent event) {
        if (checkUnsaved(Actions.CLOSE)) {
            
            if (currentSemester != null) {
                currentSemester.deleteAllClasses();
            }
            
            Platform.exit();
        } else {
            event.consume();
        }
    }
    
    private void setChanged() {
        saveMenuItem.setDisable(false);
        semesterHasChanged = true;
    }
    private boolean checkUnsaved(Actions action) {
        boolean canExit = false;
        boolean canContinue = false;
        if (semesterHasChanged) {
            AlertMessageResult result = AlertMessage.alertSaveFirstConfirmation();
            switch (result) {
                case YES:
                    if (saveSemester()) {
                        canExit = true;
                        canContinue = true;
                    } else {
                        canExit = false;
                        canContinue = false;
                    }
                    break;
                case NO:
                    canExit = true;
                    canContinue = true;
                    break;
                default:
            }
        } else {
            canExit = true;
            canContinue = true;
        }
        
        boolean out = false;
        
        if (action == Actions.CLOSE) {
            out = canExit;
        } else if (action == Actions.CONTINUE) {
            out = canContinue;
        }
        
        return out;
    }
    public void buildRecentFilesMenu() {
        // check if there are any recent files
        if (recentFiles != null) {
            // get the string array of files
            String[] files = recentFiles.getValue();

            // initialize the menu items array
            rfMenuItems = new ArrayList<>();

            // loop through the recent files array
            int i = 0;
            for (String file : files) {
                // check if the file is an object
                if (file != null) {
                    // create a new menu item with the file's name
                    MenuItem newItem = new MenuItem(file);

                    // create the click handler
                    newItem.setOnAction(this::menuItemRecentFile);

                    // add the menu item to the array
                    rfMenuItems.add(newItem);
                    i++;
                }
            }
        }
    }
    private void clearVariables() {
        currentSemester = null;
        currentClass = null;
        currentQuestion = null;
    }
    
    @FXML
    public void menuItemExit(ActionEvent event) {
        if (checkUnsaved(Actions.CLOSE)) {
            Platform.exit();
        } else {
            event.consume();
        }
    }
    @FXML
    public void menuItemAbout(ActionEvent event) {
        switchTo("About");
    }
    public void menuItemRecentFile(ActionEvent event) {
        // get the clicked on menu item
        MenuItem source = (MenuItem) event.getSource();
        
        loadClassList(recentFiles.getFilename(source.getText()), source.getText());
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //                                                              Semesters //
    ////////////////////////////////////////////////////////////////////////////
    
    @FXML
    public void menuItemNew(ActionEvent event) {
        if (checkUnsaved(Actions.CONTINUE)) {
            // show the new semester alert box and wait to see if it returns anything
            String semesterName = AlertMessage.alertTextBox("New Semester", "", "Please enter the semester name:");
            if (!"".equals(semesterName)) {
                clearVariables();
                
                //create the new semester
                currentSemester = new Semester(semesterName);

                // enable the new class button
                newClass.setDisable(false);

                // set that the semester has changed
                setChanged();
            }
        }
    }
    @FXML
    public void menuItemOpen(ActionEvent event) {
        if (checkUnsaved(Actions.CONTINUE)) {
            File semesterFile = DocumentFunctions.openDialog(stage);
            if (semesterFile != null) {
                clearVariables();
                
                loadClassList(semesterFile.toString(), semesterFile.getName());
            }
        }
    }
    @FXML
    public void menuItemSave(ActionEvent event) {
        saveSemester();
    }
    private boolean saveSemester() {
        File file = DocumentFunctions.saveDialog(stage, currentSemester.getName());
        
        if (file != null) {
            if (DocumentFunctions.writeJSON(file.toString(), currentSemester)) {
                saveMenuItem.setDisable(true);
                semesterHasChanged = false;

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //                                                                   Tabs //
    ////////////////////////////////////////////////////////////////////////////
    
    @FXML
    public void tabChange(Event event) {
        // check for the currently selected tab and the presence of a loaded class
        if (infoTab.isSelected() && currentClass != null) {
            tabInfo();
        } else if (questionTab != null && questionTab.isSelected() && currentClass != null) {
            tabQuestions();
        }
    }
    
    private void tabInfo() {
        if (currentClass != null) {
            className.setText(currentClass.getClassName());
            numQuestions.setText(currentClass.getNumQuestions().toString());

            cEdit.setDisable(false);
            cDelete.setDisable(false);
            questionTab.setDisable(false);

            if (currentClass.getNumQuestions() > 0) {
                cGrade.setDisable(false);
            } else {
                cGrade.setDisable(true);
            }
        } else {
            className.setText("");
            numQuestions.setText("");

            cEdit.setDisable(true);
            cDelete.setDisable(true);
            questionTab.setDisable(true);
            cGrade.setDisable(true);
        }
    }
    private void tabQuestions() {
        questionList.setItems(currentClass.getQuestions());

        qId.setCellValueFactory(new PropertyValueFactory("id"));
        question.setCellValueFactory(new PropertyValueFactory("text"));
        answer.setCellValueFactory(new PropertyValueFactory("answer"));
        
        if (questionList.getSelectionModel().getSelectedIndex() >= 0) {
            qDelete.setDisable(false);
            qEdit.setDisable(false);
        } else {
            qDelete.setDisable(true);
            qEdit.setDisable(true);
        }
        
        questionList.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //                                                                Classes //
    ////////////////////////////////////////////////////////////////////////////
    
    @FXML
    public void classNew(ActionEvent event) {
        String newClassName = AlertMessage.alertTextBox("New Class", "", "Please enter the class name:");
        if (!"".equalsIgnoreCase(newClassName)) {
            if (currentSemester.classExists(newClassName)) {
                AlertMessage.alertOk(AlertTypes.ALREADY_EXISTS, "class", newClassName);
            } else {
                currentClass = new SemesterClass(newClassName);
                
                currentSemester.addClass(currentClass);
                
                ObservableList classes = currentSemester.getClasses();
                int lastClassesIndex = classes.size() - 1;

                populateClassList();
                
                classList.scrollTo(lastClassesIndex);
                
                tabInfo();

                setChanged();
            }
        }
    }
    @FXML
    public void classEdit(ActionEvent event) {
        String oldName = currentClass.getClassName();
        
        String newClassName = AlertMessage.alertTextBox("Edit Class", "", "Please enter the new class name:");
        if (!"".equalsIgnoreCase(newClassName)) {
            if (currentSemester.classExists(newClassName)) {
                AlertMessage.alertOk(AlertTypes.ALREADY_EXISTS, "class", newClassName);
            } else {
                currentSemester.updateClassName(currentClass, newClassName);

                populateClassList();
                
                classTabs.getSelectionModel().select(infoTab);

                tabInfo();

                setChanged();
            }
        }
    }
    @FXML
    public void classDelete(ActionEvent event) {
        if (AlertMessage.alertYesNo(AlertTypes.DELETE_CONFIRMATION, "Are you sure that you want to delete the class \"" + currentClass.getClassName() + "\"?\n This action is irreversible.")) {
            currentClass.deleteAllQuestions();
            currentSemester.deleteClass(currentClass.getClassName());
            currentClass = null;
            
            populateClassList();
            
            tabInfo();
        }
    }
    @FXML
    public void classGrade(ActionEvent event) {
        Dse2pdVoiceGrading.currentClass = currentClass;
        Dse2pdVoiceGrading.currentSemester = currentSemester;
        
        switchTo("Grading");
    }
    @FXML
    public void classListClick(MouseEvent event) {
        if (classList.getSelectionModel().getSelectedIndex() >= 0) {
            currentClass = currentSemester.getClass(classList.getSelectionModel().getSelectedItem().toString());

            classTabs.getSelectionModel().select(infoTab);

            tabInfo();
        }
    }
    
    private void loadClassList(String filename, String semesterName) {
        currentSemester = new Semester(filename, semesterName);

        if (currentSemester.semesterExists()) {
            populateClassList();

            if (newClass.isDisabled() == true) {
                newClass.setDisable(false);
            }
        }
    }
    private void populateClassList() {
        ObservableList classes = currentSemester.getClasses();
        if (classes.size() > 0) {
            classList.getItems().clear();
            classList.setItems(classes);
        } else {
            classList.getItems().clear();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //                                                              Questions //
    ////////////////////////////////////////////////////////////////////////////
    
    @FXML
    public void questionNew(ActionEvent event) {
        String newQuestionText = AlertMessage.alertTextBox("New Question", "", "Please enter the question text:");
        // check if there was an answer
        if (!"".equalsIgnoreCase(newQuestionText)) {
            // check if the answer already exists
            if (currentClass.questionExists(newQuestionText)) {
                AlertMessage.alertOk(AlertTypes.ALREADY_EXISTS, "question", newQuestionText);
            } else {
                // show dialog to get the answer letter
                String newQuestionAnswer = AlertMessage.alertDropDown("New Question", "", "Please select the question answer:");
                
                // check if there was an answer
                if (!"".equals(newQuestionAnswer)) {
                    Question newQuestion = new Question(newQuestionText, newQuestionAnswer, currentClass.getNewQuestionId());
                    currentClass.addQuestion(newQuestion);
                    
                    tabQuestions();
                    
                    setChanged();
                }
            }
        }
    }
    @FXML
    public void questionDelete(ActionEvent event) {
        Question q = (Question) questionList.getItems().get(questionList.getSelectionModel().getSelectedIndex());
        
        if (AlertMessage.alertYesNo(AlertTypes.DELETE_CONFIRMATION, "Are you sure that you want to delete the question \"" + q.getText() + "\"?")) {
            currentClass.deleteQuestion(q.getId());
            currentQuestion = null;
            
            qDelete.setDisable(true);
            qEdit.setDisable(true);
            
            tabQuestions();
        }
    }
    @FXML
    public void questionEdit(ActionEvent event) {
        String newText = AlertMessage.alertEditTextBox("Edit Question", "", "Please enter the new question text:", currentQuestion.getText());
        if (!"".equalsIgnoreCase(newText)) {
            if (currentClass.questionExists(newText)) {
                AlertMessage.alertOk(AlertTypes.ALREADY_EXISTS, "question", newText);
            } else {
                // show dialog to get the answer letter
                String newAnswer = AlertMessage.alertDropDown("Edit Question", "", "Please select the question answer:");
                
                // check if there was an answer
                if (!"".equals(newAnswer)) {
                    currentClass.updateQuestion(currentQuestion, newText, newAnswer);
                    
                    tabQuestions();
                    
                    setChanged();
                }
            }
        }
    }    
    @FXML
    public void questionListClick(Event e) {
        if (questionList.getSelectionModel().getSelectedIndex() >= 0) {
            // enable the question buttons
            qDelete.setDisable(false);
            qEdit.setDisable(false);
            
            // get the selection model for the tableview, get the selected cells, and get the selected position
            TableView.TableViewSelectionModel selectionModel = questionList.getSelectionModel();
            ObservableList selectedCells = selectionModel.getSelectedCells();
            TablePosition tablePosition = (TablePosition) selectedCells.get(0);
            
            // get currently selected row
            int row = tablePosition.getRow();
            
            // get the question object
            currentQuestion = (Question) questionList.getItems().get(row);
        } else {
            // nullify
            currentQuestion = null;
        }
    }
}
