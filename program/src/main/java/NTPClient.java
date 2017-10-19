import org.apache.commons.net.ntp.NTPUDPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by Jan Kulose - s0557320 on 19.10.17.
 */
public class NTPClient {
    private NTPClient() {
    }

    public static LocalDateTime getTime(String hostAddress) throws UnknownHostException, IOException{
        NTPUDPClient ntpudpClient = new NTPUDPClient();
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(ntpudpClient.getTime(InetAddress.getByName(hostAddress), 123)
                .getMessage().getTransmitTimeStamp().getTime()), ZoneId.of("UTC")
        );
    }
}
