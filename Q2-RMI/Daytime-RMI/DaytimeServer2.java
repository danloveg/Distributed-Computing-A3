/**
 * DaytimeServer2.java
 *
 * Author: Daniel Lovegrove
 */

import java.io.*;
import java.rmi.*;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class DaytimeServer2 {
    private static int NUM_SECS = 1;

    public static void main(String[] args) {
        System.setProperty("java.security.policy", "./java.policy");

        try {
            if (args.length != 1) throw new IllegalArgumentException("Expects one argument");
            int port = Integer.parseInt(args[0]);
            String objName = "CurrentDate";

            // We need to set the RMI host name so the client can connect
            String host = InetAddress.getLocalHost().getHostName();
            host = "rmi://" + host + ":" + port;
            System.setProperty("java.rmi.server.hostname", host);

            // Start the registry at the specified port, in this process
            Registry registry = LocateRegistry.createRegistry(port);

            // Create an object in the registry
            System.out.println("Server started. Updating date every " + NUM_SECS + " seconds.");
            while (true) {
                // Get the current date
                Date timestamp = new Date();
                String date = timestamp.toString();
                registry.rebind(objName, new CurrentDate(date));
                // Sleep
                Thread.sleep(NUM_SECS * 1000);
            }

        } catch (NumberFormatException e) {
            System.out.println("Could not convert \"" + args[0] + "\" to an integer port number");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected one arguments. Call like:");
            System.out.println("\tjava DaytimeServer2 <port>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
