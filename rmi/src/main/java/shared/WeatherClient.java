package shared;

import client.input.WeatherData;

/**
 * Created by Jan Kulose - s0557320 on 16.11.17.
 */
public interface WeatherClient {
    void updateTemperature(MeasurePoint measurePoint);
}
