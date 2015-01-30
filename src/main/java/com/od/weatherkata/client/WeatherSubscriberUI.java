package com.od.weatherkata.client;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherSubscriberUI extends Application implements WeatherSubscriberControl {

    private final Label tempLabel = new Label();
    private final Label precipitationLabel = new Label();
    private final Label windStrengthLabel = new Label();

    private final Label lowPressureLabel = new Label();
    private final Label highPressureLabel = new Label();

    private final Label pressureDiffLabel = new Label();
    private final Label pressureDifLabel2 = new Label();
    private final Label pressureDifLabel3 = new Label();

    private WeatherSubscriber weatherSubscriber;
    private ImageView snowMobile;
    private ImageView balloon;
    private ImageView train;

    private AtomicBoolean trainEnabled = new AtomicBoolean(false);
    private AtomicBoolean balloonEnabled = new AtomicBoolean(false);
    private AtomicBoolean snowMobileEnabled = new AtomicBoolean(false);

    private volatile int tempVal;
    private volatile int windVal;
    private volatile String precipitationVal;
    private volatile int lastPressureDifference;
    private volatile int pressureDifference;
    private volatile int highPressure;
    private volatile int lowPressure;
    private TabPane tabPane;

    public void init() throws Exception {
        weatherSubscriber = new WeatherSubscriber(this);
        weatherSubscriber.subscribe();
        WeatherSubscriberChorusHandler.exportChorusHandler(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        snowMobile = createImage("Snowmobile.png");
        balloon = createImage("Balloon.png");
        train = createImage("Train.png");

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

        tabPane = new TabPane();
        Tab transport = new Tab("Weather");
        transport.setContent(vBox);
        tabPane.getTabs().add(transport);


        Region spacerA = getVerticalSpace();
        Pane pressurePane = createPressurePane();
        Region spacerB = getVerticalSpace();
        VBox pressureBox = new VBox();
        pressureBox.getChildren().addAll(spacerA, pressurePane, spacerB);

        Tab pressureTab = new Tab("Atmospheric Pressure");
        pressureTab.getStyleClass().add("tab-pane");
        pressureTab.setContent(pressureBox);
        tabPane.getTabs().add(pressureTab);

        tabPane.getStyleClass().add("tab-pane");
        return tabPane;
    }

    private Node getSnowMobileLabel() {
        return getVehicleLabel(
                "Snow Mobile:",
                "Requires:\n" +
                "Temperature <= 0");
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
                "Precipitation = Fish");
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

    private Pane createPressurePane() {
        VBox box = new VBox();
        Label label1 = new Label("Pressure Difference:");
        Label label3 = new Label();
        Label label2 = new Label();

        Label lowPres = new Label("Low Pressure");
        Label highPres = new Label("High Pressure");
        setPreferredWidth(220, label1, label2, label3);
        box.getChildren().add(getLabeledComponent(lowPres, lowPressureLabel, "pressureLabel"));
        box.getChildren().add(getLabeledComponent(highPres, highPressureLabel, "pressureLabel"));
        box.getChildren().add(getVerticalSpace(30));
        box.getChildren().add(getLabeledComponent(label1, pressureDiffLabel, "pressureLabel1"));
        box.getChildren().add(getLabeledComponent(label2, pressureDifLabel2, "pressureLabel2"));
        box.getChildren().add(getLabeledComponent(label3, pressureDifLabel3, "pressureLabel3"));
        return box;
    }

    private Node getVerticalSpace(int i) {
        Region region = new Region();
        region.setPrefHeight(i);
        return region;
    }

    private void setPreferredWidth(int i, Label... l) {
        for (Label label : l ) {
            label.setPrefWidth(i);
        }
    }

    private ImageView createImage(String url) {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(150);
        imageView.setOpacity(0.3);
        return imageView;
    }

    private void showVehicle(ImageView imageView, Boolean newValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), imageView);
        fadeTransition.setFromValue(newValue ? 0.3 : 1);
        fadeTransition.setToValue(newValue ? 1 : 0.3);
        fadeTransition.setAutoReverse(false);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
    }

    private Parent getLabeledComponent(Node label, Node control, String cssClass) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(spacer, label, spacer2, control, spacer3);
        hBox.getStyleClass().add(cssClass);
        return hBox;
    }

    @Override
    public void setTemperature(int temperature) {
        tempLabel.setText(temperature + "%");
        blink(tempLabel, 200);
        tempVal = temperature;
    }

    @Override
    public int getTemperature() {
        return tempVal;
    }

    @Override
    public void setWindStrength(int windStrength) {
        windStrengthLabel.setText(String.valueOf(windStrength));
        blink(windStrengthLabel, 200);
        windVal = windStrength;
    }

    @Override
    public int getWindStrength() {
        return windVal;
    }

    @Override
    public void setPrecipitation(String precipitation) {
        precipitationLabel.setText(precipitation);
        blink(precipitationLabel, 500);
        precipitationVal = precipitation;
    }

    @Override
    public String getPrecipitation() {
        return precipitationVal;
    }

    private void blink(Node n, int duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), n);
        fadeTransition.setFromValue(0.3);
        fadeTransition.setToValue(1);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
    }

    public void setSnowMobileEnabled(boolean enabled) {
        System.out.println("Enabling snow mobile " + enabled);
        snowMobileEnabled.set(enabled);
        showVehicle(snowMobile, enabled);
    }

    public void setBalloonEnabled(boolean enabled) {
        System.out.println("Enabling balloon " + enabled);
        balloonEnabled.set(enabled);
        showVehicle(balloon, enabled);
    }

    public void setTrainEnabled(boolean enabled) {
        System.out.println("Enabling train " + enabled);
        trainEnabled.set(enabled);
        showVehicle(train, enabled);
    }

    public void setLowPressure(int lowPressure) {
        System.out.println("Setting low pressure " + lowPressure);
        lowPressureLabel.setText(new DecimalFormat("000").format(lowPressure));
        this.lowPressure = lowPressure;
    }

    public void setHighPressure(int highPressure) {
        System.out.println("Setting low pressure " + highPressure);
        highPressureLabel.setText(new DecimalFormat("000").format(highPressure));
        this.highPressure = highPressure;
    }

    public void setPressureDifference(int difference) {
        System.out.println("Setting pressure dif " + difference);
        pressureDifLabel3.setText(pressureDifLabel2.getText());
        pressureDifLabel2.setText(pressureDiffLabel.getText());
        pressureDiffLabel.setText(new DecimalFormat("000").format(difference));
        blink(pressureDiffLabel, 200);
        this.lastPressureDifference = this.pressureDifference;
        this.pressureDifference = difference;
    }

    public int getPressureDifference() {
        return this.pressureDifference;
    }

    public int getLastPressureDifference() {
        return this.lastPressureDifference;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    @Override
    public void showPressureTab() {
        Platform.runLater(() -> {
            tabPane.getSelectionModel().select(1);
        });
    }

    @Override
    public void showWeatherTab() {
        Platform.runLater(() -> {
            tabPane.getSelectionModel().select(0);
        });
    }

    @Override
    public boolean isSnowMobileEnabled() {
        return snowMobileEnabled.get();
    }

    @Override
    public boolean isBalloonEnabled() {
        return balloonEnabled.get();
    }

    @Override
    public boolean isTrainEnabled() {
        return trainEnabled.get();
    }

    public static void main(String[] args) throws Exception {
        WeatherSubscriberUI.launch(args);
    }
}
