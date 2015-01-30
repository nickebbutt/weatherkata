package com.od.weatherkata.subscriber;

import rx.Observable;
import rx.functions.Func1;

import java.util.Map;
import java.util.Optional;

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
        connectPressure(uiControl);
        connectPressureDifference(uiControl);
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
        Observable<Boolean> windOk = windStrength.map(w -> w < 5);
        Observable<Boolean> shouldFly = Observable.combineLatest(isFish, windOk, (f, w) ->  w && ! f);
        shouldFly.distinctUntilChanged().subscribe(uiControl::setBalloonEnabled);
    }

    private void connectTrain(WeatherSubscriberControl uiControl) {
        Observable<Boolean> isFish = precipitation.map("Fish"::equals);
        Observable<Boolean> is18 = temperature.map(Integer.valueOf(18)::equals);
        Observable<Boolean> isNoWind = windStrength.map(Integer.valueOf(0)::equals);
        Observable<Boolean> canCommute = Observable.combineLatest(isFish, isNoWind, is18, (f, w, t) -> f && w && t);
        canCommute.distinctUntilChanged().subscribe(uiControl::setTrainEnabled);
    }

    private void connectPressure(WeatherSubscriberControl uiControl) {
        pressureLow.subscribe(uiControl::setLowPressure);
        pressureHigh.subscribe(uiControl::setHighPressure);
    }

    private void connectPressureDifference(WeatherSubscriberControl uiControl) {
        pressureDeltas.map(getPressureChangeFunction()).subscribe(o -> {
            o.ifPresent(uiControl::setPressureDifference);
        });
    }

    private Func1<Map<String,Integer>, Optional<Integer>> getPressureChangeFunction() {
        return new Func1<Map<String,Integer>, Optional<Integer>>() {
            int low = -1;
            int high = -1;

            @Override
            public Optional<Integer> call(Map<String, Integer> m) {
                if (m.containsKey("lowPressure")) {
                    low = m.get("lowPressure");
                }

                if (m.containsKey("highPressure")) {
                    high = m.get("highPressure");
                }

                return low != -1 && high != -1 ?
                        Optional.of(high - low) :
                        Optional.empty();
            }
        };
    }

    public void subscribe() {
        socketSubscriber.subscribe();
    }


}
