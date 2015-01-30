package com.od.weatherkata;

import org.chorusbdd.chorus.ChorusSuite;
import org.junit.runner.RunWith;

/**
 * Created by GA2EBBU on 29/01/2015.
 */
@RunWith(ChorusSuite.class)
public class RunChorus {

    public static String getChorusArgs() {
        return "-f src/test/java -h com.od.weatherkata";
    }
}
