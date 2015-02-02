package com.od.weatherkata.publisher;

import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.context.ChorusContext;
import org.chorusbdd.chorus.remoting.jmx.ChorusHandlerJmxExporter;

/**
 * Created by Nick E on 29/01/2015.
 */
@Handler("Weather Publisher")
public class WeatherPublisherChorusHandler {

    private WeatherPublisherControl weatherPublisherControl;

    public WeatherPublisherChorusHandler(WeatherPublisherControl weatherPublisherControl) {
        this.weatherPublisherControl = weatherPublisherControl;
    }

    @Step("I set the temperature to ([-\\d]+)")
    public void setTemperature(int temp) {
        weatherPublisherControl.setTemperature(temp);
        ChorusContext.getContext().put("temp", temp); //so we can check it reaches the subscriber
    }

    @Step("I set the wind strength to (\\d+)")
    public void setWind(int wind) {
        weatherPublisherControl.setWind(wind);
        ChorusContext.getContext().put("wind", wind);
    }

    @Step("I set the precipitation to (\\w+)")
    public void setPrecipitation(String precipitation) {
        weatherPublisherControl.setPrecipitation(precipitation);
        ChorusContext.getContext().put("precipitation", precipitation);
    }

    @Step("I set the pressure to (\\d+), (\\d+)")
    public void setPrecipitation(int low, int high) {
        weatherPublisherControl.setPressure(low, high);
        ChorusContext.getContext().put("pressureLow", low);
        ChorusContext.getContext().put("pressureHigh", high);
    }

    @Step("I set temp, wind and precipitation to ([-\\d]+), (\\d+), (\\w+)")
    public void setPrecipitation(int temp, int wind, String precipitation) {
        setTemperature(temp);
        setWind(wind);
        setPrecipitation(precipitation);
    }

    public static void exportChorusHandler(WeatherPublisherControl control) {
        WeatherPublisherChorusHandler handler = new WeatherPublisherChorusHandler(control);
        ChorusHandlerJmxExporter exporter = new ChorusHandlerJmxExporter(handler);
        exporter.export();
    }

}
