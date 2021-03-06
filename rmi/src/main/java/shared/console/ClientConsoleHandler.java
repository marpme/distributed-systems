package shared.console;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to register all events on the console
 * and listen for possible inputs
 */
public class ClientConsoleHandler implements Serializable {
    private static final long serialVersionUID = 46781256467L;
    private List<ConsoleEventListener> listenerList = null;
    private InputStream consoleInput = null;
    private boolean running = true;

    public ClientConsoleHandler(InputStream consoleInput) {
        listenerList = new ArrayList<>();
        this.consoleInput = consoleInput;
    }

    /**
     * Adds a custom console event listener
     *
     * @param listener reference of the listener
     */
    public void add(ConsoleEventListener listener) {
        listenerList.add(listener);
    }

    /**
     * Removes the listener by search for the reference
     *
     * @param listener reference of the listener
     */
    public void remove(ConsoleEventListener listener) {
        listenerList.remove(listener);
    }

    public void start() {
        try (Scanner scanner = new Scanner(consoleInput)) {
            while (scanner.ioException() == null && running) {
                System.out.println("Please enter:\n" +
                        "\"YYYY-MM-DD\" to receive the temperature readings for that date\n" +
                        "\"autoupdate\" to switch on/off automatic update messages (default: on)\n" +
                        "\"exit\" to exit the application");
                String message = scanner.next();
                notifyEventListeners(new ConsoleEvent(consoleInput, message));
            }
            System.out.println(scanner.ioException());
        }
    }

    /**
     * shuts the handler down and closes streams
     */
    public void shutdown() {
        running = false;
    }

    /**
     * If events get triggered by the console, then go though all
     * listeners and notify them.
     *
     * @param event reference to an event, that was triggered by the console
     */
    private void notifyEventListeners(ConsoleEvent event) {
        for (ConsoleEventListener listener : listenerList) {
            listener.onConsoleEvent(event);
        }
    }
}
