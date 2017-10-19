import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Created by Jan Kulose - s0557320 on 19.10.17.
 */
public class Entry {
    static final String HOST_ADDRESS = "3.ch.pool.ntp.org";
    public static void main(String[] args) {


        System.out.println("The current System time is " + LocalDateTime.now(Clock.systemUTC()));
        try {
            String timeResponse = NTPClient.getTime(HOST_ADDRESS).toString();
            System.out.println("The Time Server response is " + timeResponse);
        } catch (UnknownHostException e) {
            System.out.println("time server not responding");
        } catch (IOException e) {
            System.out.println("error reading time");
        }

    }

}
