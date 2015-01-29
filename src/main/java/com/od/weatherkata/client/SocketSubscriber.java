package com.od.weatherkata.client;

import javafx.application.Platform;
import org.zeromq.ZMQ;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GA2EBBU on 29/01/2015.
 */
public class SocketSubscriber {

    private BehaviorSubject<String> precipitationObservable = BehaviorSubject.create();
    private BehaviorSubject<Integer> temperatureObservable = BehaviorSubject.create();
    private BehaviorSubject<Integer> windStrengthObservable = BehaviorSubject.create();
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
                System.out.println("Subscribed to all messages");

                Pattern p = Pattern.compile("(.+):\\{(\\w+)\\}");

// Process 100 updates
                while(true) {
        // Use trim to remove the tailing '0' character
                    String string = subscriber.recvStr(0).trim();

                    Matcher matcher = p.matcher(string);
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
}
