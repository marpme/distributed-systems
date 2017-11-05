package client.request;

import client.input.ResponseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class RequestHandler<T, K extends ResponseHandler> {

    private final Socket serverSocket;
    private PrintWriter serverWriter = null;
    private BufferedReader serverReader = null;
    private T body;

    public RequestHandler(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public abstract RequestHandler<T, K> withRequestBody(T output) throws InvalidRequestBodyException;

    public K send(K responseHandler) throws IOException {
        serverWriter = new PrintWriter(serverSocket.getOutputStream());
        serverWriter.println(body);
        serverWriter.flush();

        serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        responseHandler.setResponseBody(serverReader.readLine());
        return responseHandler;
    }
}
