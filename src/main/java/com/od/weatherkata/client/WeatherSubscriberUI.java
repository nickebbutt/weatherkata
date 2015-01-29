package com.od.weatherkata.client;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherSubscriberUI extends Application implements UiControl {

    private final Label tempLabel = new Label();
    private final Label precipitationLabel = new Label();
    private final Label windStrengthLabel = new Label();
    private Stage primaryStage;
    private WeatherSubscriber weatherSubscriber;

    public void init() throws Exception {
        weatherSubscriber = new WeatherSubscriber(this);
        weatherSubscriber.subscribe();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent content = createContent(primaryStage);
        Scene scene = new Scene(content);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Weather Subscriber");

        primaryStage.setOnCloseRequest((w) -> {
            System.exit(0);
        });

        primaryStage.show();
    }

    private Parent createContent(Stage primaryStage) {
        VBox vBox = new VBox();

        Pane infoPane = createInfoPane();
        ImageView snowMobile = createImage("Snowmobile.png");
        ImageView balloon = createImage("Balloon.png");
        ImageView train = createImage("Train.png");

        //https://gist.github.com/jewelsea/1962045

        Region spacer1 = new Region();
        spacer1.setPrefHeight(30);

        Region spacerInfoPanel = new Region();
        spacerInfoPanel.setPrefHeight(30);

        Region spacer2 = new Region();
        spacer2.setPrefHeight(30);

        Region spacerTop = getVerticalSpace();

        Region spacerBottom = getVerticalSpace();

        vBox.getChildren().addAll(
            spacerTop,
            infoPane,
            spacerInfoPanel,
            getLabeledComponent(getSnowMobileLabel(), snowMobile, "transportComponent"),
            spacer1,
            getLabeledComponent(getBalloonLabel(), balloon, "transportComponent"),
            spacer2,
            getLabeledComponent(getTrainLabel(), train, "transportComponent"),
            spacerBottom
        );

        vBox.getStyleClass().add("bordered-panel");
        return vBox;

    }

    private Node getSnowMobileLabel() {
        return getVehicleLabel(
                "Snow Mobile:",
                "Requires:\n" +
                "Temperature < 0");
    }

    private Node getBalloonLabel() {
        return getVehicleLabel(
                "Balloon:",
                "Requires:\n" +
                "Wind Strength < 5\n" +
                "Precipitation != Fish");
    }

    private Node getTrainLabel() {
        return getVehicleLabel(
                "Thameslink Train:",
                "Requires:\n" +
                "Temperature = 18\n" +
                "Wind Strength = 0\n" +
                "Precipitation = None");
    }

    private Node getVehicleLabel(String label, String description) {
        Region s1 = getVerticalSpace();
        Region s2 = getVerticalSpace();
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("descLabel");

        VBox labelWithDecription = new VBox();
        labelWithDecription.getChildren().add(new Label(label));
        labelWithDecription.getChildren().add(s1);
        labelWithDecription.getChildren().add(descLabel);
        labelWithDecription.getChildren().add(s2);

        return labelWithDecription;
    }

    private Region getVerticalSpace() {
        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);
        return spacer2;
    }

    private Pane createInfoPane() {
        VBox box = new VBox();
        box.getChildren().add(getLabeledComponent(new Label("Current Temperature:"), tempLabel, "infoPanelComponent"));
        box.getChildren().add(getLabeledComponent(new Label("Current Wind Strength:"), windStrengthLabel, "infoPanelComponent"));
        box.getChildren().add(getLabeledComponent(new Label("Current Precipitation:"), precipitationLabel, "infoPanelComponent"));
        return box;
    }

    private ImageView createImage(String url) {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(150);
        return imageView;
    }

    private Parent getLabeledComponent(Node labelText, Node control, String cssClass) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(spacer, labelText, spacer2, control, spacer3);
        hBox.getStyleClass().add(cssClass);
        return hBox;
    }

    @Override
    public void setTemperature(int temperature) {
        tempLabel.setText(temperature + "%");
        blink(tempLabel);
    }

    @Override
    public void setWindStrength(int force) {
        windStrengthLabel.setText(String.valueOf(force));
        blink(windStrengthLabel);
    }

    @Override
    public void setPrecipitation(String precipitation) {
        precipitationLabel.setText(precipitation);
        blink(precipitationLabel);
    }

    private void blink(Node n) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(100), n);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
    }

    public static void main(String[] args) throws Exception {
        WeatherSubscriberUI.launch(args);
    }
}
