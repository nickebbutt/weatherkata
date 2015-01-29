package com.od.weatherkata.server;

import org.zeromq.ZMQ;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherPublisher {


    private final ZMQ.Socket publisher;

    public WeatherPublisher() throws Exception {
        ZMQ.Context context = ZMQ.context(1);

        publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:5556");
        publisher.bind("ipc://weather");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down publisher");
            publisher.close();
            context.term ();})
        );
    }

    public void sendTemperature(int temp) {
        publisher.send("temp:{" + temp + "}", 0);
    }

    public void sendWindStrength (int windStrength) {
        publisher.send("wind:{" + windStrength + "}", 0);
    }

    public void sendPrecipitation(String precipitation) {
        publisher.send("precipitation:{" + precipitation + "}", 0);
    }

}
