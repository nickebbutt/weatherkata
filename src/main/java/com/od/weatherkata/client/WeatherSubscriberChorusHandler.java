package com.od.weatherkata.client;

import com.od.weatherkata.util.Jmx;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.PassesWithin;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.context.ChorusContext;
import org.chorusbdd.chorus.remoting.jmx.ChorusHandlerJmxExporter;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by GA2EBBU on 29/01/2015.
 */
@Handler("Weather Subscriber")
public class WeatherSubscriberChorusHandler {

    private WeatherSubscriberControl uiControl;

    public WeatherSubscriberChorusHandler(WeatherSubscriberControl uiControl) {
        this.uiControl = uiControl;
    }

    @Step("I (can|can't) travel by (train|balloon|snowmobile)")
    @PassesWithin(length=1)
    public void checkCanTravel(String canOrCant, String transport) {
        uiControl.showWeatherTab();
        checkExpectedValuesSet();
        boolean expected = "can".equals(canOrCant);
        Supplier<Boolean> checker = getChecker(transport);
        assertEquals(expected, checker.get());
    }

    @Step("the pressure difference is (\\d+)")
    @PassesWithin(length=1)
    public void checkPressure(int pressureDiff) {
        uiControl.showPressureTab();
        checkExpectedValuesSet();
        assertEquals(pressureDiff, uiControl.getPressureDifference());
    }

    @Step("the last pressure difference is (\\d+)")
    @PassesWithin(length=1)
    public void checkLastPressure(int lastPressureDiff) {
        uiControl.showPressureTab();
        checkExpectedValuesSet();
        assertEquals(lastPressureDiff, uiControl.getLastPressureDifference());
    }

    private Supplier<Boolean> getChecker(String transport) {
        return "train".equals(transport) ? uiControl::isTrainEnabled :
                    "balloon".equals(transport) ? uiControl::isBalloonEnabled :
                            uiControl::isSnowMobileEnabled;
    }

    /**
     * Check that the values we set in the publisher UI have actually made it onto the subscriber UI
     * We can use the values stored in the chorus context to check this
     */
    private void checkExpectedValuesSet() {
        Map<String, Object> map = ChorusContext.getContext();
        if ( map.containsKey("temp")) {
            assertEquals((Integer)map.get("temp"), (Integer)uiControl.getTemperature() );
        }

        if ( map.containsKey("wind")) {
            assertEquals((Integer)map.get("wind"), (Integer)uiControl.getWindStrength() );
        }

        if ( map.containsKey("precipitation")) {
            assertEquals((String)map.get("precipitation"), uiControl.getPrecipitation() );
        }

        if ( map.containsKey("lowPressure")) {
            assertEquals((Integer)map.get("lowPressure"), (Integer)uiControl.getLowPressure() );
        }

        if ( map.containsKey("highPressure")) {
            assertEquals((Integer)map.get("highPressure"), (Integer)uiControl.getHighPressure() );
        }
    }

    public static void exportChorusHandler(WeatherSubscriberControl uiControl) {
        Jmx.startJmx(50124);
        WeatherSubscriberChorusHandler handler = new WeatherSubscriberChorusHandler(uiControl);
        ChorusHandlerJmxExporter exporter = new ChorusHandlerJmxExporter(handler);
        exporter.export();
    }
}
