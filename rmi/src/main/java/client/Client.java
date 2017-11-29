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
import java.util.Date;

/**
 * Client for gathering weather inforamtion form a
 * specific server
 * <p>
 * Please change the setting details inside of <FILE></FILE>
 */
public class Client implements WeatherClient {
    static String serverAddress = "localhost";
    static int serverPort = 1234;
    static boolean updateMe = true;
    static WeatherData weatherData = new WeatherData();
    static WeatherConsoleHandler handler;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            WeatherServer stub = (WeatherServer) registry.lookup(WeatherServer.class.getName());

            handler = new WeatherConsoleHandler(System.in);

            handler.add(event -> {
                if (event.getMessage().equals("exit")) {
                    System.out.println("Exiting application...");
                } else {
                        try {
                            Date day = sdf.parse(event.getMessage());
                            weatherData.addMeasurePoints(stub.getTemperatures(day));
                            handler.printCurrentWeatherData(weatherData.getDay(day));
                        } catch (RemoteException e) {
                            System.out.println(e.detail.getMessage());
                        } catch (ParseException e) {
                            System.out.println("The date is not a valid date");
                        }
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
        if (updateMe) {
//            handler.printCurrentWeatherData()
        }
    }
}
