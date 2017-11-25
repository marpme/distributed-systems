package shared;

import java.rmi.RemoteException;

public class InsufficientMeasurePointsException extends RemoteException {
    /**
     * Constructs a <code>RemoteException</code> with the specified
     * detail message.
     *
     * @param s the detail message
     */
    public InsufficientMeasurePointsException(String s) {
        super(s);
    }
}
