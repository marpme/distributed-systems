package client.request;

import client.input.WeatherResponseHandler;

import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherRequestHandler extends RequestHandler<String, WeatherResponseHandler> {

    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);


    public WeatherRequestHandler(Socket serverSocket) {
        super(serverSocket);
    }

    @Override
    public WeatherRequestHandler withRequestBody(String output) throws InvalidRequestBodyException {
        try {
            if (output.matches("\\d{4}-\\d{2}-\\d{2}")) {
                Date date = dateFormatter.parse(output);
                setBody(output);
            }
        } catch (ParseException e) {
            throw new InvalidRequestBodyException("request body wasn't in the correct date format: " + DATE_FORMAT);
        }
        return this;
    }
}
