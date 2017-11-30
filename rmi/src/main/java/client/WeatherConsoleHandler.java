package client;

import shared.MeasurePoint;

import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Weather console handler
 * It handles the preparation of showing the parsed weather information
 * inside of the console in form of a simple table.
 */
public class WeatherConsoleHandler extends ConsoleHandler {
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

    /**
     * Constructor for the console input stream
     *
     * @param consoleInput the reference to the input stream
     */
    WeatherConsoleHandler(InputStream consoleInput) {
        super(consoleInput);
    }

    /**
     * Prints the current weather information to the console
     *
     * @param weatherInformation our parsed weather information
     */
    public void printCurrentWeatherData(List<MeasurePoint> weatherInformation) {

        String statsFormat = "| %-5s | %-11s |%n";
        DecimalFormat df = new DecimalFormat("0.0 °C");

        System.out.format("+-------+-------------+%n");
        System.out.format("| Time  | Temperature |%n");
        System.out.format("+-------+-------------+%n");

        for (MeasurePoint measurePoint : weatherInformation) {
            System.out.format(statsFormat, sdf.format(measurePoint.getTimestamp()), df.format(measurePoint.getTemperature()));
        }

        System.out.format("+-------+-------------+%n");

        DoubleSummaryStatistics statistics = weatherInformation.stream()
                .map(MeasurePoint::getTemperature)
                .mapToDouble(n -> (double) n)
                .summaryStatistics();

        System.out.format(statsFormat, "AVG", df.format(statistics.getAverage()));
        System.out.format(statsFormat, "MIN", df.format(statistics.getMin()));
        System.out.format(statsFormat, "MAX", df.format(statistics.getMax()));

        System.out.format("+-------+-------------+%n");

    }

    public void printUpdatedWeatherData(Client client, MeasurePoint changedPoint) {
        List<MeasurePoint> weatherInformation = client.getWeatherData().getDay(changedPoint.getTimestamp());

        String statsFormat = "| %-5s | %-11s |";
        DecimalFormat df = new DecimalFormat("0.0 °C");

        System.out.format("+-------+-------------+%n");
        System.out.format("| Time  | Temperature |%n");
        System.out.format("+-------+-------------+%n");

        for (MeasurePoint measurePoint : weatherInformation) {
            String prefix = "";
            String suffix = "";
            if (measurePoint == changedPoint) {
                prefix = "*";
                suffix = "*";
            }
            System.out.print(prefix);
            System.out.format(statsFormat, sdf.format(measurePoint.getTimestamp()), df.format(measurePoint.getTemperature()));
            System.out.println(suffix);
        }

        System.out.format("+-------+-------------+%n");

        DoubleSummaryStatistics statistics = weatherInformation.stream()
                .map(MeasurePoint::getTemperature)
                .mapToDouble(n -> (double) n)
                .summaryStatistics();

        System.out.format(statsFormat, "AVG", df.format(statistics.getAverage()));
        System.out.format(statsFormat, "MIN", df.format(statistics.getMin()));
        System.out.format(statsFormat, "MAX", df.format(statistics.getMax()));

        System.out.format("+-------+-------------+%n");
    }
}
