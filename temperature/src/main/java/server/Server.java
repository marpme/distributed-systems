package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class Server {

    final static int PORT = 1234;
    final static ExecutorService executer = Executors.newCachedThreadPool();
    static ServerSocket socket;

    public static void main(String[] args) {
        try {
            socket = new ServerSocket(PORT);
            while (true) {
                executer.execute(new TemperatureHandler(socket.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

