package com.od.weatherkata.client;

/**
 * Created by GA2EBBU on 29/01/2015.
 */
public interface UiControl {
    void setTemperature(int temperature);

    void setWindStrength(int force);

    void setPrecipitation(String precipitation);
}
