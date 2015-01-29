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

        temperature.subscribe(uiControl::setTemperature);
        windStrength.subscribe(uiControl::setWindStrength);
        precipitation.subscribe(uiControl::setPrecipitation);

        temperature.map(i -> i <= 0).distinctUntilChanged().subscribe(uiControl::setSnowMobileEnabled);

        Observable<Boolean> isFish = precipitation.map("Fish"::equals);
        Observable<Boolean> shouldFly = Observable.combineLatest(isFish, windStrength, (f, w) -> w < 5 && ! f);
        shouldFly.distinctUntilChanged().subscribe(uiControl::setBalloonEnabled);

    }

    public void subscribe() {
        socketSubscriber.subscribe();
    }
}
