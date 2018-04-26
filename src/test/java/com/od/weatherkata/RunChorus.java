package com.od.weatherkata;

import org.chorusbdd.chorus.ChorusSuite;
import org.junit.runner.RunWith;

/**
 * Created by Nick E on 29/01/2015.
 */
@RunWith(ChorusSuite.class)
public class RunChorus {

    /**
     * @return command line arguments for Chorus (see www.chorus-bdd.org for docs)
     * 
     * To enable extra logging set -l to 'info' or 'debug' 
     */
    public static String getChorusArgs() {
        return "-f src/test/java -h com.od.weatherkata -l warn";
    }
}
