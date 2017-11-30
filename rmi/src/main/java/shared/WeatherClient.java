package shared;

import client.input.WeatherData;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Jan Kulose - s0557320 on 16.11.17.
 */
public interface WeatherClient extends Remote {
    void updateTemperature(MeasurePoint measurePoint) throws RemoteException;
}
