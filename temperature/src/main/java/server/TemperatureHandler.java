package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemperatureHandler implements Runnable {

    private final Socket clientSocket;
    private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private TemperatureHandler() {
        clientSocket = new Socket();
    }

    TemperatureHandler(Socket clientSocket) {
        System.out.println("Preparing for new client.");
        this.clientSocket = clientSocket;
    }

    private Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("Client connected to our service. Serving Temperature.");
        try (InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader clientReader = new BufferedReader(inputStreamReader)) {
            String clientRequest = clientReader.readLine();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("the actual request was " + clientRequest);

            // Date parsing
            Date dateOfTemp;
            try {
                dateOfTemp = SDF.parse(clientRequest);
            } catch (ParseException pe) {
                pe.printStackTrace();
                out.write("2;Parse Exception for given date.");
                return;
            }
            System.out.println("Registered Date was " + dateOfTemp.toString());

            // Temp history
            Optional<TemperatureHistory> history = TemperatureReader.getInstance().receiveTemperatureForDate(dateOfTemp);
            String response = history
                    .map(data -> "0;" + data.getWeatherData().stream().collect(Collectors.joining(";")))
                    .orElse("1;No weather data found for your date.");
            out.println(response);
            System.out.println("The server's response was (shortened):");
            System.out.println(response.length() > 20 ?
                    response.substring(0, 20) + "(...)":
                    response.substring(0, response.length()-1) + "(...)");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
