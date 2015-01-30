package com.od.weatherkata.client;

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


    public WeatherSubscriber(WeatherSubscriberControl uiControl) {
        connectStatusPanel(uiControl);
        connectSnowMobile(uiControl);
        connectBalloon(uiControl);
        connectTrain(uiControl);
        connectPressureDifference(uiControl);
    }

    private void connectPressureDifference(WeatherSubscriberControl uiControl) {
        Observable<Integer> pressureDif = Observable.combineLatest(pressureLow, pressureHigh, (l, h) -> h-l);
        pressureDif.distinctUntilChanged().subscribe(uiControl::setPressureDifference);
    }

    private void connectStatusPanel(WeatherSubscriberControl uiControl) {
        temperature.distinctUntilChanged().subscribe(uiControl::setTemperature);
        windStrength.distinctUntilChanged().subscribe(uiControl::setWindStrength);
        precipitation.distinctUntilChanged().subscribe(uiControl::setPrecipitation);
    }

    private void connectSnowMobile(WeatherSubscriberControl uiControl) {
        temperature.map(i -> i <= 0).distinctUntilChanged().subscribe(uiControl::setSnowMobileEnabled);
    }

    private void connectBalloon(WeatherSubscriberControl uiControl) {
        Observable<Boolean> isFish = precipitation.map("Fish"::equals);
        Observable<Boolean> shouldFly = Observable.combineLatest(isFish, windStrength, (f, w) -> w < 5 && ! f);
        shouldFly.distinctUntilChanged().subscribe(uiControl::setBalloonEnabled);
    }

    private void connectTrain(WeatherSubscriberControl uiControl) {
        Observable<Boolean> isFish = precipitation.map("Fish"::equals);
        Observable<Boolean> is18 = temperature.map(Integer.valueOf(18)::equals);
        Observable<Boolean> isNoWind = windStrength.map(Integer.valueOf(0)::equals);
        Observable<Boolean> canCommute = Observable.combineLatest(isFish, isNoWind, is18, (f, w, t) -> f && w && t);
        canCommute.distinctUntilChanged().subscribe(uiControl::setTrainEnabled);
    }

    public void subscribe() {
        socketSubscriber.subscribe();
    }
}
