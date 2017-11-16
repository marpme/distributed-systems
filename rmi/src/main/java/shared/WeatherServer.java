package shared;

import server.TemperatureHistory;
import shared.WeatherClient;

import java.rmi.Remote;
import java.util.Date;

public interface WeatherServer extends Remote {

    TemperatureHistory getTemperatures(Date searchDate);

    boolean register(WeatherClient client);

    boolean deregister(WeatherClient client);

}
