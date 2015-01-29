package com.od.weatherkata.server;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherPublisherUI extends Application {

    private Stage primaryStage;
    private WeatherPublisher publisher;

    public void init() throws Exception {
        publisher = new WeatherPublisher();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent content = createContent(primaryStage);
        Scene scene = new Scene(content);
        primaryStage.setScene(scene);
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.setTitle("Weather Publisher");
        primaryStage.setOnCloseRequest((w) -> {System.exit(0);});
        primaryStage.show();
    }

    private Parent createContent(Stage primaryStage) {
        VBox vBox = new VBox();


        //https://gist.github.com/jewelsea/1962045

        Slider tempSlider = new Slider(-20, 50, 0.5);
        tempSlider.setShowTickLabels(true);
        tempSlider.setMajorTickUnit(10);
        tempSlider.setMinorTickCount(10);
        tempSlider.setShowTickMarks(true);
        tempSlider.setBlockIncrement(1);
        tempSlider.setSnapToTicks(true);
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
        final ComboBox<String> precipitationCombo = new ComboBox<>(options);
        precipitationCombo.valueProperty().addListener( (v, o, n) -> {
            publisher.sendPrecipitation(n);
        });

        Region spacer2 = new Region();
        spacer2.setPrefHeight(30);

        Slider windStrengthSlider = new Slider(0, 10, 1);
        windStrengthSlider.setShowTickLabels(true);
        windStrengthSlider.setMajorTickUnit(1);
        windStrengthSlider.setMinorTickCount(0);
        windStrengthSlider.setShowTickMarks(true);
        windStrengthSlider.setBlockIncrement(1);
        windStrengthSlider.setSnapToTicks(true);
        windStrengthSlider.valueProperty().addListener( (v, o, n) -> {
            publisher.sendWindStrength(n.intValue()); ;}
        );


        Region spacerTop = new Region();
        VBox.setVgrow(spacerTop, Priority.ALWAYS);

        Region spacerBottom = new Region();
        VBox.setVgrow(spacerBottom, Priority.ALWAYS);

        vBox.getChildren().addAll(
                spacerTop,
                getLabeledComponent("Temperature:", tempSlider),
                spacer1,
                getLabeledComponent("Precipitation:", precipitationCombo),
                spacer2,
                getLabeledComponent("Wind Strength:", windStrengthSlider),
                spacerBottom
        );

//        BorderPane borderPane = new BorderPane();
//        borderPane.setCenter(vBox);
        return vBox;
    }

    private Parent getLabeledComponent(String labelText, Control control) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Region spacer2 = new Region();
        spacer2.setPrefWidth(20);

        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);

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
