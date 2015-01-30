package com.od.weatherkata.client;

import javafx.application.Platform;
import org.zeromq.ZMQ;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GA2EBBU on 29/01/2015.
 */
public class SocketSubscriber {

    private BehaviorSubject<String> precipitationObservable = BehaviorSubject.create();
    private BehaviorSubject<Integer> temperatureObservable = BehaviorSubject.create();
    private BehaviorSubject<Integer> windStrengthObservable = BehaviorSubject.create();

    private BehaviorSubject<Integer> lowPressureObservable = BehaviorSubject.create();
    private BehaviorSubject<Integer> highPressureObservable = BehaviorSubject.create();
    private BehaviorSubject<Map<String, Integer>> pressureDeltasObservable = BehaviorSubject.create();

    private ZMQ.Socket subscriber;
    private ZMQ.Context context;

    public void subscribe() {
        Runnable runnable = new Runnable() {
            public void run() {
                context = ZMQ.context(1);

// Socket to talk to server
                System.out.println("Collecting updates from weather server");
                subscriber = context.socket(ZMQ.SUB);
                subscriber.connect("tcp://localhost:5556");

                subscriber.subscribe("temp".getBytes());
                subscriber.subscribe("wind".getBytes());
                subscriber.subscribe("precipitation".getBytes());
                subscriber.subscribe("pressure".getBytes());
                System.out.println("Subscribed to all messages");

                Pattern p = Pattern.compile("(.+):\\{([-,\\w]+)\\}");

                int lastLow = -1, lastHigh = -1;

// Process 100 updates
                while(true) {
        // Use trim to remove the tailing '0' character
                    String message = subscriber.recvStr(0).trim();
                    System.out.println("Received " + message);

                    Matcher matcher = p.matcher(message);
                    boolean match = matcher.matches();
                    if ( match ) {
                        String type = matcher.group(1);
                        String val = matcher.group(2);
                        switch(type) {
                            case "temp" :
                                runLater(() -> {
                                    temperatureObservable.onNext(Integer.valueOf(val));
                                });
                                break;
                            case "wind" :
                                runLater(() -> {
                                    windStrengthObservable.onNext(Integer.valueOf(val));
                                });
                                break;
                            case "precipitation" :
                                runLater(() -> {
                                    precipitationObservable.onNext(val);
                                });
                                break;
                            case "pressure" :
                                String[] lowAndHigh = val.split(",");
                                int low = Integer.parseInt(lowAndHigh[0]);
                                int high = Integer.parseInt(lowAndHigh[1]);

                                runLater(() -> {
                                    lowPressureObservable.onNext(low);
                                });

                                runLater(() -> {
                                    highPressureObservable.onNext(high);
                                });

                                Map<String,Integer> deltas = new HashMap<String,Integer>();
                                if ( lastLow != low) {
                                    deltas.put("lowPressure", low);
                                    lastLow = low;
                                }

                                if ( lastHigh != high) {
                                    deltas.put("highPressure", high);
                                    lastHigh = high;
                                }

                                if ( ! deltas.isEmpty()) {
                                    pressureDeltasObservable.onNext(deltas);
                                }

                        }
                    }
                }
            }
        };

        new Thread(runnable).start();
    }

    //run on the JFX thread
    private void runLater(Runnable r) {
        Platform.runLater(r);
    }

    public Observable<String> getPrecipitationObservable() {
        return precipitationObservable;
    }

    public Observable<Integer> getTemperatureObservable() {
        return temperatureObservable;
    }

    public Observable<Integer> getWindStrengthObservable() {
        return windStrengthObservable;
    }

    public BehaviorSubject<Integer> getHighPressureObservable() {
        return highPressureObservable;
    }

    public BehaviorSubject<Integer> getLowPressureObservable() {
        return lowPressureObservable;
    }

    public BehaviorSubject<Map<String, Integer>> getPressureDeltasObservable() {
        return pressureDeltasObservable;
    }
}
