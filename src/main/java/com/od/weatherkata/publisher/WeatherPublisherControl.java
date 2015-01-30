package com.od.weatherkata.publisher;

/**
 * Created by GA2EBBU on 29/01/2015.
 */
public interface WeatherPublisherControl {


    void setTemperature(int temp);

    void setWind(int wind);

    void setPrecipitation(String precipitation);

    void setPressure(int low, int high);
}
