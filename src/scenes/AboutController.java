package scenes;

import dse2pdvoicegrading.Dse2pdVoiceGrading;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Darren Eidson
 */
public class AboutController extends Switchable implements Initializable {

    @FXML
    ImageView imageView;
    
    @FXML
    AnchorPane root;
    
    AreaChart<Number,Number> areaChart;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Dse2pdVoiceGrading.stage.setOnCloseRequest(this::exitApplication);
            Number[] timesOctober = new Number[]{
                null, // 1
                null, // 2
                null, // 3
                null, // 4
                null, // 5
                null, // 6
                null, // 7
                null, // 8
                null, // 9
                null, // 10
                null, // 11
                null, // 12
                null, // 13
                null, // 14
                null, // 15
                null, // 16
                null, // 17
                null, // 18
                null, // 19
                null, // 20
                null, // 21
                null, // 22
                null, // 23
                null, // 24
                null, // 25
                null, // 26
                null, // 27
                null, // 28
                3, // 29
                1, // 30
                0, // 31
            };

            Number[] timesNovember = new Number[]{
                0, // 1
                4, // 2
                4, // 3
                2, // 4
                5, // 5
                0, // 6
                0, // 7
                0, // 8
                0, // 9
                0, // 10
                0, // 11
                0, // 12
                0, // 13
                0, // 14
                0, // 15
                0, // 16
                0, // 17
                0, // 18
                0, // 19
                2, // 20
                11, // 21
                0, // 22
                6, // 23
                2, // 24
                null, // 25
                null, // 26
                null, // 27
                null, // 28
                null, // 29
                null, // 30
                null, // 31
            };

            NumberAxis xAxis = new NumberAxis(1, 31, 1);
            xAxis.setMinorTickCount(0);
            NumberAxis yAxis = new NumberAxis();
            yAxis.setMinorTickCount(0);
            areaChart = new AreaChart<>(xAxis,yAxis);
            areaChart.setTitle("Hours Worked");

            areaChart.setLayoutX(14);
            areaChart.setLayoutY(250);
            areaChart.setPrefSize(750, 300);

            XYChart.Series seriesOct = new XYChart.Series();
            seriesOct.setName("October");
            for (int i = 0; i < timesOctober.length; i++) {
                if (timesOctober[i] != null) {
                    seriesOct.getData().add(new XYChart.Data(i + 1, timesOctober[i]));
                }
            }

            XYChart.Series seriesNov = new XYChart.Series();
            seriesNov.setName("November");
            for (int i = 0; i < timesNovember.length; i++) {
                if (timesNovember[i] != null) {
                    seriesNov.getData().add(new XYChart.Data(i + 1, timesNovember[i]));
                }
            }

            areaChart.getData().addAll(seriesOct, seriesNov);

            areaChart.setLegendSide(Side.RIGHT);

            root.getChildren().add(areaChart);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }   
    
    @FXML
    public void exitApplication(WindowEvent event) {
        Platform.exit();
    }
    
    @FXML
    public void returnButton(ActionEvent event) {
        switchTo("MainScene");
    }
}
