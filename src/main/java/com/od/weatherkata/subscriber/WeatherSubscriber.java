package com.od.weatherkata.subscriber;

import rx.Observable;

import java.util.Map;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherSubscriber {

    private SocketSubscriber socketSubscriber = new SocketSubscriber();

    private Observable<String> precipitation = socketSubscriber.getPrecipitationObservable();
    private Observable<Integer> temperature = socketSubscriber.getTemperatureObservable();
    private Observable<Integer> windStrength = socketSubscriber.getWindStrengthObservable();
    private Observable<Integer> pressureLow = socketSubscriber.getLowPressureObservable();
    private Observable<Integer> pressureHigh = socketSubscriber.getHighPressureObservable();
    private Observable<Map<String,Integer>> pressureDeltas = socketSubscriber.getPressureDeltasObservable();

    private WeatherSubscriberControl uiControl;

    public WeatherSubscriber(WeatherSubscriberControl uiControl) {
        this.uiControl = uiControl;
        connectStatusPanel();
        connectSnowMobile();
        connectBalloon();
        connectTrain();
        connectPressure();
        connectPressureDifference();
    }

    private void connectStatusPanel() {
        //TODO - provide code to set the temperature, precipitation and wind strength
        //TODO - suppress sending any duplicate values which are unchanged
        //temperature.subscribe(System.out::println);

        uiControl.setTemperature(0);
        uiControl.setPrecipitation("Unknown");
        uiControl.setWindStrength(0);
    }

    private void connectSnowMobile() {
        //TODO - provide code to enable the snow mobile if the temperature is < 0
    }

    private void connectBalloon() {
        //TODO - provide code to enable the balloon if the wind is < 5 and the precipitation != Fish
    }

    private void connectTrain() {
        //TODO - provide code to enable the Thameslink train if the wind is 0, temp = 18 and the precipitation == Fish
    }

    private void connectPressure() {
        //TODO - provide code to set the low pressure and high pressure
    }

    private void connectPressureDifference() {
        //TODO - provide code to set the difference in pressure
    }

    public void subscribe() {
        socketSubscriber.subscribe();
    }

}
