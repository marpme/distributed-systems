package client;

import client.input.WeatherData;
import client.input.WeatherResponseHandler;
import client.request.InvalidRequestBodyException;
import client.request.WeatherRequestHandler;
import shared.MeasurePoint;
import shared.WeatherClient;
import shared.WeatherServer;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;

/**
 * Client for gathering weather inforamtion form a
 * specific server
 *
 * Please change the setting details inside of <FILE></FILE>
 */
public class Client implements WeatherClient {
    static String serverAddress = "localhost";
    static int serverPort = 1234;
    static Socket serverSocket;

    public static void main(String[] args) {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            WeatherServer stub = (WeatherServer) registry.lookup(WeatherServer.class.getName());

            WeatherConsoleHandler handler = new WeatherConsoleHandler(System.in);

            handler.add(event -> {
                if (event.getMessage().matches("\\d{4}-\\d{2}-\\d{2}")) {
                    try {
                        List<MeasurePoint> measurePoints = stub.getTemperatures(new Date(1990,10,10));

                        handler.printCurrentWeatherData(measurePoints);

                    } catch (IOException e) {
                        System.out.println("The server had some problems processing our request. Please try again later.");
                    }
                }
            });

            handler.add((ConsoleEvent event) -> {
                if (event.getMessage().equals("exit")) {
                    System.out.println("Exiting application...");
                    System.exit(0);
                }
            });

            // blocking
            handler.start();

        } catch (RemoteException re) {
            re.printStackTrace();
        } catch (NotBoundException nbe) {
            nbe.printStackTrace();
        }





    }

    @Override
    public void updateTemperature(WeatherData measurePoint) {

    }
}
