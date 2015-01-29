package com.od.weatherkata.client;

import rx.Observable;
import rx.functions.Func2;

import java.util.function.BiFunction;

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
    }

    private void connectStatusPanel(WeatherSubscriberControl uiControl) {
        temperature.distinctUntilChanged().subscribe(uiControl::setTemperature);
        windStrength.distinctUntilChanged().subscribe(uiControl::setWindStrength);
        precipitation.distinctUntilChanged().subscribe(uiControl::setPrecipitation);
    }

    private void enableSnowMobile(WeatherSubscriberControl uiControl) {
        temperature.map(i -> i <= 0).distinctUntilChanged().forEach(uiControl::setSnowMobileEnabled);
    }

    private void enableBalloon(WeatherSubscriberControl uiControl) {
        Observable<Boolean> isFish = precipitation.map("Fish"::equals);
        Observable<Boolean> shouldFly = Observable.combineLatest(isFish, windStrength, (f, w) -> w < 5 && ! f);
        shouldFly.distinctUntilChanged().forEach(uiControl::setBalloonEnabled);
    }

    public void subscribe() {
        socketSubscriber.subscribe();
    }
}
