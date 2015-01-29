package com.od.weatherkata.client;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherSubscriberUI extends Application {

    private final Label tempLabel = new Label();
    private final Label precipitationLabel = new Label();
    private final Label windStrengthLabel = new Label();
    private Stage primaryStage;

    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent content = createContent(primaryStage);
        Scene scene = new Scene(content);
        primaryStage.setScene(scene);
        primaryStage.setWidth(500);
        primaryStage.setHeight(800);
        primaryStage.setTitle("Weather Subscriber");
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


        Region spacer2 = new Region();
        spacer2.setPrefHeight(30);


        Region spacerTop = new Region();
        VBox.setVgrow(spacerTop, Priority.ALWAYS);

        Region spacerBottom = new Region();
        VBox.setVgrow(spacerBottom, Priority.ALWAYS);

        vBox.getChildren().addAll(
                spacerTop,
                infoPane,
                getLabeledComponent("Snow Mobile:", snowMobile),
                spacer1,
                getLabeledComponent("Balloon:", balloon),
                spacer2,
                getLabeledComponent("Train:", train),
                spacerBottom
        );

//        BorderPane borderPane = new BorderPane();
//        borderPane.setCenter(vBox);
        return vBox;
    }

    private Pane createInfoPane() {
        VBox box = new VBox();
        box.getChildren().add(getLabeledComponent("Current Temperature:", tempLabel));
        box.getChildren().add(getLabeledComponent("Current Wind Strength:", windStrengthLabel));
        box.getChildren().add(getLabeledComponent("Current Precipitation:", precipitationLabel));
        return box;
    }

    private ImageView createImage(String url) {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(250);
        return imageView;
    }

    private Parent getLabeledComponent(String labelText, Node control) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Region spacer2 = new Region();
        spacer2.setPrefWidth(20);

        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        Label label = new Label(labelText);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(spacer, label, spacer2, control, spacer3);
        return hBox;
    }

    public static void main(String[] args) throws Exception {
        WeatherSubscriberUI.launch(args);
    }
}
