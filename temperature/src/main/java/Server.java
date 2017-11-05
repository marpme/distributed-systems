import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class Server {

    // Port beliebig
    // Threads und RMI secure
    // Stream communication (TCP)
    // Close reader after use!

    // Client.Client -> requestToServer(YYYY-MM-dd)
    // Server -> response(status;value;value;value;value;value...) starting with 0 o'clock,
    // status 0 for success, 1 for error
    //

    static ServerSocket socket;
    static Socket clientSocket;
    final static int PORT = 1234;

    public static void main(String[] args) {

        try {
            socket = new ServerSocket(PORT);
            clientSocket = socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//        double[][] temp = new double[7][24];
//        for (int i = 0; i < temp.length; i++) {
//            for (int j = 0; j < temp[i].length; j++) {
//                if (j == 0 && i == 0) {
//                    temp[i][j] = (Math.random() - 0.5) * 3.0;
//                } else if (j == 0) {
//                    temp[i][j] = temp[i - 1][23] + Math.random() + 0.5;
//                } else if (j < 12) {
//                    temp[i][j] = temp[i][j - 1] + Math.random() + 0.5;
//                } else {
//                    temp[i][j] = temp[i][j - 1] - (Math.random() + 0.5);
//                }
//            }
//        }
//
//        for (int i = 0; i < temp[0].length; i++) {
//            for (int j = 0; j < temp.length; j++) {
//                System.out.printf("%.1f;", temp[j][i]);
//            }
//            System.out.println();
//        }