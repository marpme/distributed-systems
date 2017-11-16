package server;

import shared.MeasurePoint;
import shared.WeatherClient;
import shared.WeatherServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class Server implements WeatherServer {

    final static int PORT = 1234;
    private static Registry registry;
    private List<WeatherClient> clients;

    @Override
    public List<MeasurePoint> getTemperatures(Date searchDate) {
        return TemperatureReader.getInstance().receiveMeasurePoints(searchDate);
    }

    @Override
    public boolean register(WeatherClient client) {
        return clients.add(client);
    }

    @Override
    public boolean deregister(WeatherClient client) {
        return clients.remove(client);
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

