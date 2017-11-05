package client.input;

import client.ResponseStatus;
import com.sun.istack.internal.Nullable;

import java.util.DoubleSummaryStatistics;
import java.util.List;

public class WeatherData {

    private final ResponseStatus status;

    private final String errorMessage;

    private List<Double> temperatures = null;

    private DoubleSummaryStatistics summaryStatistics = null;

    public WeatherData(ResponseStatus status, @Nullable String errorMessage, List<Double> temperatures, DoubleSummaryStatistics summaryStatistics) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.temperatures = temperatures;
        this.summaryStatistics = summaryStatistics;
    }

    public WeatherData(ResponseStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Double> getTemperatures() {
        return temperatures;
    }

    public DoubleSummaryStatistics getSummaryStatistics() {
        return summaryStatistics;
    }
}
