package com.od.weatherkata.subscriber;

/**
 * Created by Nick E on 29/01/2015.
 */
public interface WeatherSubscriberControl {

    void setTemperature(int temperature);

    void setWindStrength(int force);

    void setPrecipitation(String precipitation);

    void setSnowMobileEnabled(boolean enabled);

    void setBalloonEnabled(boolean enabled);

    void setTrainEnabled(boolean enabled);

    public void setLowPressure(int lowPressure);

    public void setHighPressure(int highPressure);

    public void setPressureDifference(int highPressure);

    int getTemperature();

    int getWindStrength();

    String getPrecipitation();

    boolean isSnowMobileEnabled();

    boolean isBalloonEnabled();

    boolean isTrainEnabled();

    int getPressureDifference();

    int getLastPressureDifference();

    int getHighPressure();

    int getLowPressure();

    void showPressureTab();

    void showWeatherTab();
}
