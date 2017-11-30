package server;

import shared.InsufficientMeasurePointsException;
import shared.MeasurePoint;
import shared.WeatherClient;
import shared.WeatherServer;
import shared.console.WeatherConsoleListener;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class Server implements WeatherServer {

    final static int PORT = 1234;
    private static final Logger log = Logger.getLogger(Server.class.getName());
    private static Registry registry;
    private Thread consoleListener;
    private List<WeatherClient> clients = new CopyOnWriteArrayList<>();

    public Server() {
        this.consoleListener = new Thread(new WeatherConsoleListener(System.in, this.clients));
        this.consoleListener.start();
    }

    public static void main(String[] args) {
        try {
            registry = LocateRegistry.createRegistry(PORT);

            Server server = new Server();
            WeatherServer stub = (WeatherServer) UnicastRemoteObject.exportObject(server, PORT);

            registry.rebind(WeatherServer.class.getName(), stub);
            log.info("Startup succeeded");
            log.info("RMI bind on port: " + PORT);
        } catch (RemoteException e) {
            log.log(Level.SEVERE, "Startup failed: ", e);
        }
    }

    @Override
    public List<MeasurePoint> getTemperatures(Date searchDate) throws RemoteException {
        log.info("Client requested weather information for Date " + searchDate);

        List<MeasurePoint> measurePoints = TemperatureReader.getInstance().receiveMeasurePoints(searchDate);
        if (measurePoints.size() != 24) {
            String exception = "Insufficient measurements for the given date: " + searchDate;
            log.warning(exception);
            throw new InsufficientMeasurePointsException(exception);
        }

        return measurePoints;
    }

    @Override
    public boolean register(WeatherClient client) throws RemoteException {
        log.info("Client registered successfully.");
        return clients.add(client);
    }

    @Override
    public boolean deregister(WeatherClient client) throws RemoteException {
        log.info("Client deregistered successfully.");
        return clients.remove(client);
    }
}

