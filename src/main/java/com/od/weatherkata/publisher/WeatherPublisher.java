package com.od.weatherkata.publisher;

import org.zeromq.ZMQ;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by GA2EBBU on 27/01/2015.
 */
public class WeatherPublisher {

    private final ZMQ.Socket publisher;

    private volatile int temp = 0;
    private volatile int wind = 0;
    private volatile String precipitation = "Unknown";

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

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

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            doSendWind();
            doSendTemp();
            doSendPrecipitation();
        }, 2, 2, TimeUnit.SECONDS);
    }


    public void sendTemperature(int temp) {
        this.temp = temp;
        scheduledExecutorService.execute(this::doSendTemp);
    }

    private void doSendTemp() {
        doSend("temp:{" + temp + "}");
    }

    public void sendWindStrength (int windStrength) {
        this.wind = windStrength;
        scheduledExecutorService.execute(this::doSendWind);
    }

    private void doSendWind() {
        doSend("wind:{" + wind + "}");
    }


    public void sendPrecipitation(String precipitation) {
        this.precipitation = precipitation;
        scheduledExecutorService.execute(this::doSendPrecipitation);
    }

    private void doSendPrecipitation() {
        doSend("precipitation:{" + precipitation + "}");
    }



    public void sendPressure(int lowPressure, int highPressure) {
        scheduledExecutorService.execute(() -> { doSendPressure(lowPressure, highPressure); });
    }

    private void doSendPressure(int low, int high) {
        //send atomically, don't autosend periodically like the others
        doSend("pressure:{" + low + "," + high + "}");
    }

    private void doSend(String s) {
        System.out.println("Sending: " + s);
        publisher.send(s, 0);
    }
}
