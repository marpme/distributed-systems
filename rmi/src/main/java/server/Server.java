package server;

import shared.WeatherClient;
import shared.WeatherServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class Server implements WeatherServer {

    final static int PORT = 1234;
    private static Registry registry;

    @Override
    public TemperatureHistory getTemperatures(Date searchDate) {
        return null;
    }

    @Override
    public boolean register(WeatherClient client) {
        return false;
    }

    @Override
    public boolean deregister(WeatherClient client) {
        return false;
    }

    public static void main(String[] args) {
        try {
            registry = LocateRegistry.createRegistry(PORT);

            Server server = new Server();
            WeatherServer stub = (WeatherServer) UnicastRemoteObject.exportObject(server, PORT);

            registry.rebind(WeatherServer.class.getName(), stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

