package com.od.weatherkata.client;

/**
 * Created by GA2EBBU on 29/01/2015.
 */
public interface WeatherSubscriberControl {

    void setTemperature(int temperature);

    int getTemperature();

    void setWindStrength(int force);

    int getWindStrength();

    void setPrecipitation(String precipitation);

    String getPrecipitation();

    void setSnowMobileEnabled(boolean enabled);

    void setBalloonEnabled(boolean enabled);

    void setTrainEnabled(boolean enabled);

    boolean isSnowMobileEnabled();

    boolean isBalloonEnabled();

    boolean isTrainEnabled();

}
