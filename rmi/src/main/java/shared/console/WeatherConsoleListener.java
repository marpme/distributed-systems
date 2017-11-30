package shared.console;

import server.TemperatureReader;
import shared.MeasurePoint;
import shared.WeatherClient;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WeatherConsoleListener implements Runnable {

    private InputStream systemIn;
    private List<WeatherClient> clients;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH");

    public WeatherConsoleListener(InputStream systemIn, List<WeatherClient> clients) {
        this.systemIn = systemIn;
        this.clients = clients;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(this.systemIn)) {
            while (scanner.ioException() == null) {
                System.out.println(
                        "Please enter:\n" +
                                ">  \"DD-MM-YYYY HH;Temperature\" to push an client update\n"
                );

                String[] messages = scanner.nextLine().split(";");

                if (messages.length != 2) {
                    System.out.println("Your current input wasn't valid please try again!");
                } else {
                    try {
                        Date myDate = parseDate(messages[0]);
                        Float temperature = parseTemperature(messages[1].replace(',', '.'));

                        Optional<MeasurePoint> ms = TemperatureReader.getInstance().receiveExactMeasurePoints(myDate);
                        if (ms.isPresent()) {
                            ms.get().setTemperature(temperature);
                            clients.forEach(client -> {
                                try {
                                    client.updateTemperature(ms.get());
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            });
                            System.out.println(clients.size() + " Client(s) has been informed about this change!");
                        } else {
                            System.out.println("Cannot update measure point for given date, because no data are available.");
                        }

                    } catch (NumberFormatException | ParseException e) {
                        System.out.println("Invalid Input, Please try again.");
                    }
                }

                System.out.println();
            }
        }
    }

    private Date parseDate(String maybeDate) throws ParseException {
        return sdf.parse(maybeDate);
    }

    private float parseTemperature(String maybeFloat) throws NumberFormatException {
        return Float.parseFloat(maybeFloat);
    }

}
