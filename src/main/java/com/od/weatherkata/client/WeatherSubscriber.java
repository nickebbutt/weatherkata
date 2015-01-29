package com.od.weatherkata.client;

import org.zeromq.ZMQ;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherSubscriber {

    public static void main (String[] args) {
        final ZMQ.Context context = ZMQ.context(1);

// Socket to talk to server
        System.out.println("Collecting updates from weather server");
        final ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://localhost:5556");

        subscriber.subscribe("temp".getBytes());
        subscriber.subscribe("wind".getBytes());
        subscriber.subscribe("precipitation".getBytes());
        System.out.println("Subscribed to all messages");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            subscriber.close();
            context.term();
        }));

// Process 100 updates
        while(true) {
// Use trim to remove the tailing '0' character
            String string = subscriber.recvStr(0).trim();
            System.out.println(string);
        }

    }
}
