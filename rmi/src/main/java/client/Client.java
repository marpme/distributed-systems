package client;

import client.input.WeatherData;
import shared.MeasurePoint;
import shared.WeatherClient;
import shared.WeatherServer;

import java.io.IOException;
import java.io.Serializable;
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
public class Client implements WeatherClient, Serializable {
    private static final long serialVersionUID = 878457870984L;
    private String serverAddress = "localhost";
    private int serverPort = 1234;
    private boolean updateMe = true;
    private WeatherData weatherData = new WeatherData();
    private WeatherConsoleHandler handler;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void run(String[] args) {
        System.out.println("Client started.");

        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            WeatherServer stub = (WeatherServer) registry.lookup(WeatherServer.class.getName());
            stub.register(this);

            handler = new WeatherConsoleHandler(System.in);

            handler.add(event -> {
                if (event.getMessage().trim().equals("exit")) {
                    System.out.println("Exiting application...");
                    handler = null;
                    weatherData = null;
                    try {
                        stub.deregister(this);
                    } catch (RemoteException e) {
                        System.out.println("error during shutdown of client: " + e.getMessage());
                    }
                    System.exit(0);
                } else if (event.getMessage().trim().equals("autoupdate on")) {
                    System.out.println("Autoupdate turned on");
                    updateMe = true;
                } else if (event.getMessage().trim().equals("autoupdate off")) {
                    System.out.println("Autoupdate turned off");
                    updateMe = true;
                } else {
                    try {
                        Date day = sdf.parse(event.getMessage().trim());
                        weatherData.addMeasurePoints(stub.getTemperatures(day));
                        handler.printCurrentWeatherData(weatherData.getDay(day));
                        System.out.println();
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
            System.out.println("We have encountered an error processing the request:\n" + re.getMessage());
        }


    }

    @Override
    public void updateTemperature(MeasurePoint measurePoint) {
        weatherData.modifyMeasurePoint(measurePoint);
        if (updateMe) {
            handler.printUpdatedWeatherData(this, measurePoint);
        }
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }
}
