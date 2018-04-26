package com.od.weatherkata;

import org.chorusbdd.chorus.ChorusSuite;
import org.junit.runner.RunWith;

/**
 * Created by Nick E on 29/01/2015.
 */
@RunWith(ChorusSuite.class)
public class RunChorus {

    /**
     * Command line arguments for Chorus interpreter
     * See www.chorus-bdd.org for docs
     * 
     * Use -l debug or -l info to turn on extra logging
     */
    public static String getChorusArgs() {
        return "-f src/test/java -h com.od.weatherkata -l warn";
    }
}
