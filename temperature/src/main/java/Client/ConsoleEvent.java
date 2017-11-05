package Client;

import java.io.InputStream;
import java.util.EventObject;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class ConsoleEvent extends EventObject {
    private static final long serialVersionUID = -2348723947398111324L;
    private String message;

    public ConsoleEvent(InputStream source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
