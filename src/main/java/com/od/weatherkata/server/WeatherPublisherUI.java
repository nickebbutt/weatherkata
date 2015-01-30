package com.od.weatherkata.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherPublisherUI extends Application implements WeatherPublisherControl {

    private WeatherPublisher publisher;
    private Slider tempSlider;
    private Slider windStrengthSlider;
    private ComboBox<String> precipitationCombo;
    private Slider lowPressure;
    private Slider highPressure;
    private Button sendButton;
    private TabPane tabPane;

    public void init() throws Exception {
        publisher = new WeatherPublisher();
        WeatherPublisherChorusHandler.exportChorusHandler(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent content = createContent(primaryStage);
        Scene scene = new Scene(content);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Weather Publisher");
        scene.getStylesheets().add("stylesheetPublisher.css");
        primaryStage.setOnCloseRequest((w) -> {
            System.exit(0);
        });

        primaryStage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        double xCenter = ((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        double yCenter = ((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

        primaryStage.setX(xCenter - 200);
        primaryStage.setY(yCenter - 200);
    }

    private Parent createContent(Stage primaryStage) {
        VBox vBox = new VBox();

        //https://gist.github.com/jewelsea/1962045

        tempSlider = new Slider(-20, 50, 0);
        tempSlider.setShowTickLabels(true);
        tempSlider.setMajorTickUnit(10);
        tempSlider.setMinorTickCount(1);
        tempSlider.setShowTickMarks(true);
        tempSlider.setBlockIncrement(1);
        tempSlider.setSnapToTicks(false);
        tempSlider.valueProperty().addListener( (v, o, n) -> {
            publisher.sendTemperature(n.intValue()) ;}
        );

        Region spacer1 = new Region();
        spacer1.setPrefHeight(30);

        ObservableList<String> options =
            FXCollections.observableArrayList(
                "None",
                "Rain",
                "Snow",
                "Hail",
                "Fish"   //http://www.bbc.co.uk/news/world-asia-27298939
            );
        precipitationCombo = new ComboBox<>();
        precipitationCombo.setItems(options);
        precipitationCombo.valueProperty().addListener((v, o, n) -> {
            publisher.sendPrecipitation(n);
        });

        Region spacer2 = new Region();
        spacer2.setPrefHeight(30);

        windStrengthSlider = new Slider(0, 10, 0);
        windStrengthSlider.setShowTickLabels(true);
        windStrengthSlider.setMajorTickUnit(1);
        windStrengthSlider.setMinorTickCount(0);
        windStrengthSlider.setShowTickMarks(true);
        windStrengthSlider.setBlockIncrement(1);
        windStrengthSlider.setSnapToTicks(true);
        windStrengthSlider.valueProperty().addListener((v, o, n) -> {
                    publisher.sendWindStrength(n.intValue());
                }
        );


        Region spacerTop = createVerticalGlue();

        Region spacerBottom = createVerticalGlue();

        vBox.getChildren().addAll(
                spacerTop,
                getLabeledComponent("Temperature:", tempSlider),
                spacer1,
                getLabeledComponent("Precipitation:", precipitationCombo),
                spacer2,
                getLabeledComponent("Wind Strength:", windStrengthSlider),
                spacerBottom
        );

        vBox.getStyleClass().add("bordered-panel");


        Pane pressureBox = createPressureBox();

        tabPane = new TabPane();

        Tab weatherTab = new Tab("Weather");
        weatherTab.setContent(vBox);
        tabPane.getTabs().add(weatherTab);

        Tab pressureTab = new Tab("Atmospheric Pressure");
        pressureTab.setContent(pressureBox);
        tabPane.getTabs().addAll(pressureTab);
        return tabPane;
    }

    private Pane createPressureBox() {
        lowPressure = new Slider(0, 1000, 400);
        lowPressure.setShowTickLabels(true);
        lowPressure.setMajorTickUnit(100);
        lowPressure.setMinorTickCount(2);
        lowPressure.setShowTickMarks(true);
        lowPressure.setBlockIncrement(10);
        lowPressure.setSnapToTicks(false);

        highPressure = new Slider(0, 1000, 600);
        highPressure.setShowTickLabels(true);
        highPressure.setMajorTickUnit(100);
        highPressure.setMinorTickCount(1);
        highPressure.setShowTickMarks(true);
        highPressure.setBlockIncrement(10);
        highPressure.setSnapToTicks(false);

        linkSliders();

        Region s2 = createVerticalGlue();
        Region s3 = createVerticalGlue();

        sendButton = new Button("Send");

        HBox buttonBox = new HBox();
        Region r = getHorizontalGlue();
        buttonBox.getChildren().addAll(r, sendButton);
        sendButton.setOnAction(e -> { sendPressure(); });
        buttonBox.getStyleClass().add("sendButton");

        VBox vbox = new VBox();
        Region s1 = new Region();
        s1.setPrefHeight(10);
        vbox.getChildren().add(s2);
        vbox.getChildren().add(getLabeledComponent("Pressure Low", lowPressure));
        vbox.getChildren().add(s1);
        vbox.getChildren().add(getLabeledComponent("Pressure High", highPressure));
        vbox.getChildren().add(s3);
        vbox.getChildren().add(buttonBox);
        return vbox;
    }

    private void sendPressure() {
        publisher.sendPressure(
            (int) lowPressure.getValue(),
            (int) highPressure.getValue()
        );
    }

    private Region getHorizontalGlue() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    //don't allow them to cross
    private void linkSliders() {
        highPressure.valueProperty().addListener((v, o, n) -> {
                double lowVal = lowPressure.getValue();
                if ( n.doubleValue() < lowVal) {
                highPressure.setValue(lowVal);
            }
        }
        );

        lowPressure.valueProperty().addListener((v, o, n) -> {
                double highVal = highPressure.getValue();
                if ( n.doubleValue() > highVal) {
                    lowPressure.setValue(highVal);
                }
            }
        );
    }

    private Region createVerticalGlue() {
        Region s3 = new Region();
        VBox.setVgrow(s3, Priority.ALWAYS);
        return s3;
    }

    public void setTemperature(int temp) {
        Platform.runLater( () -> {
            tabPane.getSelectionModel().select(0);
            tempSlider.setValue(temp);});
    }

    public void setWind(int wind) {
        Platform.runLater(() -> {
            tabPane.getSelectionModel().select(0);
            windStrengthSlider.setValue(wind);
        });
    }

    public void setPrecipitation(String precipitation) {
        Platform.runLater( () -> {
            tabPane.getSelectionModel().select(0);
            precipitationCombo.setValue(precipitation);});
    }

    public void setPressure(int low, int high) {
        Platform.runLater( () -> {
            tabPane.getSelectionModel().select(1);
            if ( low < highPressure.getValue()) {
                lowPressure.setValue(low);
                highPressure.setValue(high);
            } else {
                highPressure.setValue(high);
                lowPressure.setValue(low);
            }

            sendPressure();
        });
    }

    private Parent getLabeledComponent(String labelText, Control control) {
        Region spacer = getHorizontalGlue();

        Region spacer2 = new Region();
        spacer2.setPrefWidth(20);

        Region spacer3 = getHorizontalGlue();

        Label label = new Label(labelText);

        control.setPrefWidth(200);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(spacer, label, spacer2, control, spacer3);
        return hBox;
    }

    public static void main(String[] args) throws Exception {
        WeatherPublisherUI.launch(args);
    }
}
