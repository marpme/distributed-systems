import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;


/**
 * Wrapper class for the NTPUDPClient (Apache)
 */
public class NTPClient {

    private final NTPUDPClient ntpudpClient;

    public NTPClient() {
        ntpudpClient = new NTPUDPClient();
    }

    public NTPClient(NTPUDPClient ntpudpClient) {
        this.ntpudpClient = ntpudpClient;
    }

    /**
     * Receives the Time packet from the chosen NTP server
     *
     * @param hostAddress the host address you chose
     * @return an optional time info
     */
    private Optional<TimeInfo> getTimeInfo(String hostAddress) {
        Optional<TimeInfo> timeInfo = Optional.empty();
        try {
            timeInfo = Optional.of(ntpudpClient.getTime(InetAddress.getByName(hostAddress), 123));
        } catch (IOException e) {
            System.err.println("Host couldn't be found. Please make sure you entered a correct host.");
        }
        return timeInfo;
    }

    /**
     * Parses the milliseconds timestamp for the time the packet
     * was transmitted
     *
     * @param timeInfoOptional the received time info packet
     * @return returns 0 if the server wasn't reachable or a timestamp if he was reachable
     */
    private Long getPacketTimestamp(Optional<TimeInfo> timeInfoOptional) {
        return timeInfoOptional
                .map(TimeInfo::getMessage)
                .map(NtpV3Packet::getTransmitTimeStamp)
                .map(TimeStamp::getTime)
                .orElse(0L);
    }

    /**
     * Receives the current time for a given host
     *
     * @param hostAddress the host you chose
     * @return a LocalDateTime with the time of the host
     */
    public LocalDateTime getTime(String hostAddress) {

        // Return the correct time when the packet was transmitted
        return LocalDateTime.ofInstant(
                // Millisecond epoch time -> Instant
                Instant.ofEpochMilli(getPacketTimestamp(getTimeInfo(hostAddress))),
                // UTC timezone
                ZoneId.of("UTC")
        );
    }
}
