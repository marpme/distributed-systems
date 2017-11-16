package server;

import java.rmi.Remote;
import java.util.Date;
import java.util.List;

public interface WeatherServer extends Remote {

    TemperatureHistory getTemperatures(Date searchDate);

    boolean register(WeatherClient client);

    boolean deregister(WeatherClient client);

}
