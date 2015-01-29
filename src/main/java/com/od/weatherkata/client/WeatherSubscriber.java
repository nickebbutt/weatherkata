package com.od.weatherkata.client;

import rx.Observable;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherSubscriber {

    private WeatherSubscriberControl uiControl;
    private SocketSubscriber socketSubscriber = new SocketSubscriber();

    private Observable<String> precipitation = socketSubscriber.getPrecipitationObservable();
    private Observable<Integer> temperature = socketSubscriber.getTemperatureObservable();
    private Observable<Integer> windStrength = socketSubscriber.getWindStrengthObservable();

    public WeatherSubscriber(WeatherSubscriberControl uiControl) {
        this.uiControl = uiControl;
        connectStatusPanel(uiControl);
        enableSnowMobile(uiControl);
        enableBalloon(uiControl);
        enableTrain(uiControl);
    }

    private void enableTrain(WeatherSubscriberControl uiControl) {
        Observable<Boolean> isFish = precipitation.map("Fish"::equals);
        Observable<Boolean> is18 = temperature.map(Integer.valueOf(18)::equals);
        Observable<Boolean> isNoWind = windStrength.map(Integer.valueOf(0)::equals);
        Observable<Boolean> canCommute = Observable.combineLatest(isFish, isNoWind, is18, (f, w, t) -> f && w && t);
        canCommute.distinctUntilChanged().subscribe(uiControl::setTrainEnabled);
    }

    private void connectStatusPanel(WeatherSubscriberControl uiControl) {
        temperature.distinctUntilChanged().subscribe(uiControl::setTemperature);
        windStrength.distinctUntilChanged().subscribe(uiControl::setWindStrength);
        precipitation.distinctUntilChanged().subscribe(uiControl::setPrecipitation);
    }

    private void enableSnowMobile(WeatherSubscriberControl uiControl) {
        temperature.map(i -> i <= 0).distinctUntilChanged().subscribe(uiControl::setSnowMobileEnabled);
    }

    private void enableBalloon(WeatherSubscriberControl uiControl) {
        Observable<Boolean> isFish = precipitation.map("Fish"::equals);
        Observable<Boolean> shouldFly = Observable.combineLatest(isFish, windStrength, (f, w) -> w < 5 && ! f);
        shouldFly.distinctUntilChanged().subscribe(uiControl::setBalloonEnabled);
    }

    public void subscribe() {
        socketSubscriber.subscribe();
    }
}
