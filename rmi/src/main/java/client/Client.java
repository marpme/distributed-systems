package client;

import client.input.WeatherData;
import shared.MeasurePoint;
import shared.WeatherClient;
import shared.WeatherServer;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Client for gathering weather inforamtion form a
 * specific server
 * <p>
 * Please change the setting details inside of <FILE></FILE>
 */
class Client implements WeatherClient, Serializable {

    private static final long serialVersionUID = 878457870984L;
    private String serverAddress = "localhost";
    private int serverPort = 1234;

    private boolean updateMe = true;

    private WeatherData weatherData = new WeatherData();
    private WeatherConsoleHandler handler;
    private WeatherServer stub;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    void run(String[] args) {
        System.out.println("Client started.");

        // shutdown hook for deregistering
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(serverAddress, serverPort);
            stub = (WeatherServer) registry.lookup(WeatherServer.class.getName());

            handler = new WeatherConsoleHandler(System.in);

            WeatherClient clientStub = (WeatherClient) UnicastRemoteObject.exportObject(this, 0);
            // registering client
            stub.register(clientStub);

            handler.add(event -> {
                if (event.getMessage().trim().equals("exit")) {
                    System.exit(0);
                } else if (event.getMessage().contains("autoupdate")) {
                    this.updateMe = !this.updateMe;
                    System.out.println(updateMe
                            ? "Automatic updates turned - on -!"
                            : "Automatic updates turned - off -!");
                } else {
                    try {
                        Date day = sdf.parse(event.getMessage().trim());
                        weatherData.addMeasurePoints(stub.getTemperatures(day));
                        handler.printCurrentWeatherData(weatherData.getDay(day));
                        System.out.println();
                    } catch (RemoteException e) {
                        System.out.println(e.detail.getMessage());
                    } catch (ParseException e) {
                        System.out.println("Invalid input.");
                    }
                }
                System.out.println();
            });

            // blocking
            handler.start();

        } catch (RemoteException | NotBoundException re) {
            System.out.println("We have encountered an error processing the request:\n" + re.getMessage());
        }
    }

    @Override
    public void updateTemperature(MeasurePoint measurePoint) throws RemoteException {
        weatherData.modifyMeasurePoint(measurePoint);
        if (this.updateMe) {
            handler.printUpdatedWeatherData(this, measurePoint);
        }
    }

    WeatherData getWeatherData() {
        return weatherData;
    }

    private void shutdown() {
        System.out.println("Exiting application...");
        handler.shutdown();
        handler = null;
        weatherData = null;
        try {
            stub.deregister(this);
        } catch (RemoteException e) {
            System.out.println("Error during deregistering of client: " + e.getMessage());
        }
    }
}
