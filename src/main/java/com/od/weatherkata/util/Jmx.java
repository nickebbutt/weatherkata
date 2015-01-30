package com.od.weatherkata.util;

import org.chorusbdd.chorus.remoting.jmx.ChorusHandlerJmxExporter;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

/**
 * Created by GA2EBBU on 29/01/2015.
 */
public class Jmx {


    public static void startJmx(int jmxPort) {
        try {
            LocateRegistry.createRegistry(jmxPort);

            //        System.setProperty("com.sun.management.jmxremote.port","50123");
            //        System.setProperty("com.sun.management.jmxremote.authenticate","false");
            //        System.setProperty("com.sun.management.jmxremote.ssl","false");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();

            JMXServiceURL jmxUrl
                    = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + jmxPort + "/jmxrmi");
            JMXConnectorServer connectorServer
                    = JMXConnectorServerFactory.newJMXConnectorServer(jmxUrl, null, server);

            connectorServer.start();

            System.setProperty(ChorusHandlerJmxExporter.JMX_EXPORTER_ENABLED_PROPERTY, "true");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
