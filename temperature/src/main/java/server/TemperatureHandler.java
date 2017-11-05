package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
        try {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientRequest = clientReader.readLine();
            System.out.println("the actual response was " + clientRequest);
            Date dateOfTemp = parseDate(clientRequest);
            System.out.println("Registered Date was " + dateOfTemp.toString());
            Optional<TemperatureHistory> history = TemperatureReader.getInstance().receiveTemperatureForDate(dateOfTemp);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String response = history
                    .map(data -> "0;" + data.getWeatherData().stream().collect(Collectors.joining(";")))
                    .orElse("1;No weather data found for your date.");
            out.println(response);
        } catch (IOException e) {
            //TODO Client is maybe dead, destroy server executor.
            e.printStackTrace();
        }
    }

    private Date parseDate(String dateResponse) {
        try {
            return SDF.parse(dateResponse);
        } catch (Exception e) {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.write("Parse Exception for given date.");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        return null;
    }

}
