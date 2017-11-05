package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class Server {

    // Port beliebig
    // Threads und RMI secure
    // Stream communication (TCP)
    // Close reader after use!

    // Client -> requestToServer(YYYY-MM-dd)
    // server.Server -> response(status;value;value;value;value;value...) starting with 0 o'clock,
    // status 0 for success, 1 for error
    //

    final static int PORT = 1234;
    final static ExecutorService executer = Executors.newCachedThreadPool();
    static ServerSocket socket;
    static Socket clientSocket;

    public static void main(String[] args) {

        List k = TemperatureReader.getInstance().receiveTemperatureForDate("22.22.2222");

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

