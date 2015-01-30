package com.od.weatherkata.util;

import org.chorusbdd.chorus.remoting.jmx.ChorusHandlerJmxExporter;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Nick E on 29/01/2015.
 */
public class Jmx {


    public static void startJmx(int jmxPort) {
        try {

            //        System.setProperty("com.sun.management.jmxremote.port","50123");
            //        System.setProperty("com.sun.management.jmxremote.authenticate","false");
            //        System.setProperty("com.sun.management.jmxremote.ssl","false");
            Registry r = LocateRegistry.createRegistry(jmxPort);
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();

            JMXServiceURL jmxUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + jmxPort + "/jmxrmi");
            JMXConnectorServer connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxUrl, null, server);

            connectorServer.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    //try to clean up on shutdown
                    connectorServer.stop();
                } catch (IOException e) {
                    System.out.println("Shutting down JMX");
                    e.printStackTrace();
                }
            }));

            System.setProperty(ChorusHandlerJmxExporter.JMX_EXPORTER_ENABLED_PROPERTY, "true");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
