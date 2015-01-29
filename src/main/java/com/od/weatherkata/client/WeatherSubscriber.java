package com.od.weatherkata.client;

import rx.Observable;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherSubscriber {

    private UiControl uiControl;
    private SocketSubscriber socketSubscriber = new SocketSubscriber();

    private Observable<String> precipitation = socketSubscriber.getPrecipitationObservable();
    private Observable<Integer> temperature = socketSubscriber.getTemperatureObservable();
    private Observable<Integer> windStrength = socketSubscriber.getWindStrengthObservable();

    public WeatherSubscriber(UiControl uiControl) {
        this.uiControl = uiControl;

        temperature.subscribe(uiControl::setTemperature);
        windStrength.subscribe(uiControl::setWindStrength);
        precipitation.subscribe(uiControl::setPrecipitation);
    }

    public void subscribe() {
        socketSubscriber.subscribe();
    }
}
