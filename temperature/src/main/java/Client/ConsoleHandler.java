package Client;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Jan Kulose - s0557320 on 02.11.17.
 */
public class ConsoleHandler {
    private List<ConsoleEventListener> listenerList = null;
    private InputStream consoleInput = null;

    public ConsoleHandler(InputStream consoleInput) {
        this.listenerList = new ArrayList<>();
        this.consoleInput = consoleInput;
    }

    public void add(ConsoleEventListener listener) {
        this.listenerList.add(listener);
    }

    public void remove(ConsoleEventListener listener) {
        this.listenerList.remove(listener);
    }

    public void start() {
        try (Scanner scanner = new Scanner(consoleInput)) {
            while (scanner.ioException() == null) {
                System.out.println("Please enter:\n" +
                        "\"YYYY-MM-DD\" to receive the temperature readings for that date\n" +
                        "\"exit\" to exit the application");
                String message = scanner.next();
                notifyEventListeners(new ConsoleEvent(consoleInput, message));
            }
            System.out.println(scanner.ioException());
        }
    }

    private void notifyEventListeners(ConsoleEvent event) {
        for (ConsoleEventListener listener : listenerList) {
            listener.onConsoleEvent(event);
        }
    }
}
