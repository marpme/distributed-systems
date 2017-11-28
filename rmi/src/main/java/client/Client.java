package client;

import client.input.WeatherData;
import shared.WeatherClient;
import shared.WeatherServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Client for gathering weather inforamtion form a
 * specific server
 * <p>
 * Please change the setting details inside of <FILE></FILE>
 */
public class Client implements WeatherClient {
    static String serverAddress = "localhost";
    static int serverPort = 1234;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            WeatherServer stub = (WeatherServer) registry.lookup(WeatherServer.class.getName());

            WeatherConsoleHandler handler = new WeatherConsoleHandler(System.in);

            handler.add(event -> {
                if (event.getMessage().matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
                    try {
                        handler.printCurrentWeatherData(stub.getTemperatures(sdf.parse(event.getMessage())));
                    } catch (RemoteException e) {
                        System.out.println(e.detail.getMessage());
                    } catch (ParseException e) {
                        System.out.println("The date is not a valid date");
                    }
                } else if (!event.getMessage().contains("exit")) {
                    System.out.println("The date is not a valid date");
                    System.out.println("Please try again with the following format: 'YYYY-MM-DD'");
                }
                System.out.println();
            });

            handler.add((ConsoleEvent event) -> {
                if (event.getMessage().equals("exit")) {
                    System.out.println("Exiting application...");
                    System.exit(0);
                }
            });

            // blocking
            handler.start();

        } catch (RemoteException | NotBoundException re) {
            re.printStackTrace();
        }


    }

    @Override
    public void updateTemperature(WeatherData measurePoint) {

    }
}
