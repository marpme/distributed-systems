package client;

import client.input.WeatherData;

import java.io.InputStream;
import java.text.DecimalFormat;

public class WeatherConsoleHandler extends ConsoleHandler {
    WeatherConsoleHandler(InputStream consoleInput) {
        super(consoleInput);
    }

    public void printCurrentWeatherData(WeatherData weatherInformation) {
        if (ResponseStatus.OK == weatherInformation.getStatus()) {

            String leftAlignFormat = "| %02d:00 | %-11s |%n";
            String statsFormat = "| %-5s | %-11s |%n";

            System.out.format("+-------+-------------+%n");
            System.out.format("| Time  | Temperature |%n");
            System.out.format("+-------+-------------+%n");

            for (int i = 0; i < weatherInformation.getTemperatures().size(); i++) {
                System.out.format(leftAlignFormat, i, weatherInformation.getTemperatures().get(i) + " °C");
            }
            System.out.format("+-------+-------------+%n");

            DecimalFormat df = new DecimalFormat("0.0 °C");
            System.out.format(statsFormat, "AVG", df.format(weatherInformation.getSummaryStatistics().getAverage()));
            System.out.format(statsFormat, "MIN", df.format(weatherInformation.getSummaryStatistics().getMin()));
            System.out.format(statsFormat, "MAX", df.format(weatherInformation.getSummaryStatistics().getMax()));

            System.out.format("+-------+-------------+%n");
        } else {
            System.out.println("Best error on world: " + weatherInformation.getErrorMessage());
        }
    }
}
