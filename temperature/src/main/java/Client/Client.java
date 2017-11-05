package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class Client {
    static String serverAddress = "";
    static int serverPort = 0;
    static Socket serverSocket;

    public static void main(String[] args) {
        ConsoleHandler handler = new ConsoleHandler(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");

        handler.add(new ConsoleEventListener() {
            @Override
            public void onConsoleEvent(ConsoleEvent event) {
                if (event.getMessage().matches("\\d{4}-\\d{2}-\\d{2}")) {
                    try {
                        String date = sdf.parse(event.getMessage()).toString();
                        System.out.println(date + " is a valid date");
                    } catch (ParseException e) {
                        System.out.println("could not parse date");
                        e.printStackTrace();
                    }
                };
            }
        });

        handler.add(new ConsoleEventListener() {
            @Override
            public void onConsoleEvent(ConsoleEvent event) {
                if (event.getMessage().equals("exit")) {
                    System.out.println("Exiting application...");
                    System.exit(0);
                }
            }
        });

        // blocking
        handler.start();

//        try {
//            serverSocket = new Socket(serverAddress, serverPort);
//
//            PrintWriter serverWriter = new PrintWriter(serverSocket.getOutputStream());
//            serverWriter.println("My message");
//            serverWriter.flush();
//
//            BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
//            serverReader.readLine();
//
//            serverSocket.shutdownInput();
//            serverSocket.close(); // complete closing input and output
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}
