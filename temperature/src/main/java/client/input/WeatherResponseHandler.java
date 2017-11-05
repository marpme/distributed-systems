package client.input;

import client.ResponseStatus;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherResponseHandler extends ResponseHandler<WeatherData> {

    @Override
    public WeatherData getParsedOutput() {
        List<String> response = Arrays.stream(getResponseBody().split(";")).collect(Collectors.toList());

        int statusCode = Integer.parseInt(response.remove(0));
        ResponseStatus status = ResponseStatus.fromInt(statusCode);

        String errorMessage = null;
        
        if (status == ResponseStatus.ERROR) {
            errorMessage = response.get(0);
            return new WeatherData(status, errorMessage);
        } else if (status == ResponseStatus.UNKNOWN) {
            errorMessage = "Our client doesn't support this type of error.";
            return new WeatherData(status, errorMessage);
        }


        List<Double> temperatures = response.stream().mapToDouble(Double::parseDouble).boxed().collect(Collectors.toList());
        DoubleSummaryStatistics statistics = response.stream().mapToDouble(Double::parseDouble).summaryStatistics();

        return new WeatherData(status, errorMessage, temperatures, statistics);
    }
}
