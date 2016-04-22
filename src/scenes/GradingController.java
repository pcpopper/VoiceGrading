package scenes;

import dse2pdvoicegrading.Actions;
import dse2pdvoicegrading.Dse2pdVoiceGrading;
import dse2pdvoicegrading.Question;
import dse2pdvoicegrading.SemesterClass;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Darren Eidson
 */
public class GradingController extends Switchable implements Initializable {

    private SemesterClass currentClass;
    
    private boolean loadError = false;
    
    private int qTot = 0;
    private int qCorrect = 0;
    private int qCurrent = 1;
    
    private String answer;
    
    private Integer[] questionIds;
    
//    private Thread speechRecognition;     TO IMPLEMENT
    
    @FXML
    public Label lblClass;
    @FXML
    public Label lblQCurr;
    @FXML
    public Label lblQTot;
    @FXML
    public Label lblQ1;
    @FXML
    public Label lblQ2;
    @FXML
    public Label lblQ3;
    @FXML
    public Label lblQ4;
    @FXML
    public Label lblQ5;
    @FXML
    public Label lblA1;
    @FXML
    public Label lblA2;
    @FXML
    public Label lblA3;
    @FXML
    public Label lblA4;
    @FXML
    public Label lblA5;
    @FXML
    public Label lblCorrect;
    @FXML
    public Label lblPercent;
    @FXML
    public Label lblText;
    
    @FXML
    public Button btnQ1;
    @FXML
    public Button btnQ2;
    @FXML
    public Button btnQ3;
    @FXML
    public Button btnQ4;
    @FXML
    public Button btnQ5;
    @FXML
    public Button btnPrev;
    @FXML
    public Button btnNext;
    @FXML
    public Button btnRestart;
    @FXML
    public Button btnFinish;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentClass = Dse2pdVoiceGrading.currentClass;
        
        if (currentClass.getNumQuestions() > 0) {
            qTot = currentClass.getNumQuestions();
            
            lblClass.setText(currentClass.getClassName());
            lblQTot.setText("" + qTot);
            
            questionIds = currentClass.getQuestionIds();
            
            loadQuestion();
            
//            Dse2pdVoiceGrading.stage.setOnCloseRequest(this::exitApplication);     TO IMPLEMENT
        }
//        try {
//            if (currentClass == null) {
//                throw new Exception("Error loading the class for grading, please try again.");
//            }
//        } catch (Exception e) {
//            Dse2pdVoiceGrading.alertErrorMessage(e).showAndWait();
//            switchTo("MainScene");
//        } finally {
//        }
    }
    
//    @FXML
//    public void exitApplication(WindowEvent event) {     TO IMPLEMENT
//        if (checkUnsaved(Actions.CLOSE)) {
//            
//            if (currentSemester != null) {
//                currentSemester.deleteAllClasses();
//            }
//            
//            Platform.exit();
//        } else {
//            event.consume();
//        }
//    }
    
    @FXML
    public void gradeQuestionButtons(ActionEvent event) {
        String source = event.getSource().toString();
        source = source.substring(source.lastIndexOf(']') + 1).replace("'", "").trim();
        
        if (source.equalsIgnoreCase(answer)) {
            qCorrect++;
        }
        
        populateLabels();
        qCurrent++;
        
        if (qCurrent <= qTot) {
            loadQuestion();
        } else {
            
        }
    }
    @FXML
    public void prevButton(ActionEvent event) {
        qCurrent--;
        loadQuestion();
    }
    @FXML
    public void nextButton(ActionEvent event) {
        qCurrent++;
        loadQuestion();
    }
    @FXML
    public void restartButton(ActionEvent event) {
        qCurrent = 0;
        qCorrect = 0;
        populateLabels();
        qCurrent = 1;
        loadQuestion();
    }
    @FXML
    public void finishButton(ActionEvent event) {
        switchTo("MainScene");
    }
    
    private void populateLabels() {
        lblCorrect.setText(qCorrect + "/" + (qCurrent));
        int percent = (int) Math.round(((double) qCorrect / (double) qCurrent) * 100);
        lblPercent.setText(percent + "%");
        lblQCurr.setText("" + qCurrent);
    }
    private void loadQuestion() {
        Question q = currentClass.getQuestion(questionIds[qCurrent - 1]);
        
        if (q != null && !"".equals(q.getText()) && !"".equals(q.getAnswer())) {
            lblText.setText(q.getText());
            answer = q.getAnswer();
            
            btnQ1.setDisable(false);
            btnQ2.setDisable(false);
            btnQ3.setDisable(false);
            btnQ4.setDisable(false);
            btnQ5.setDisable(false);
            
            if (qCurrent > 1) {
                btnPrev.setDisable(false);
                btnRestart.setDisable(false);
            } else {
                btnPrev.setDisable(true);
                btnRestart.setDisable(true);
            }
            
            if (qCurrent == qTot) {
                btnNext.setDisable(true);
            } else {
                btnNext.setDisable(false);
            }
        }
    }
}
