package client.input;

import shared.MeasurePoint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Weather data storing class
 */
public class WeatherData implements Serializable {
    private static final long serialVersionUID = 1082528412728L;

    /**
     * All temperature data
     */
    private List<MeasurePoint> measurePoints = null;

    public WeatherData() {
        this.measurePoints = new ArrayList<>();
    }

    public void addMeasurePoints(List<MeasurePoint> measurePoints) {
        if (measurePoints.stream()
                .filter(distinctByKey(MeasurePoint::getTimestamp))
                .collect(Collectors.toList())
                .size() != measurePoints.size()) {
            throw new IllegalArgumentException("Duplicate data found in request");
        }
        this.measurePoints.addAll(measurePoints);
    }

    public void modifyMeasurePoint(MeasurePoint measurePoint) {
        measurePoints.removeIf(point -> point.getTimestamp() == measurePoint.getTimestamp());
        measurePoints.add(measurePoint);
    }

    public List<MeasurePoint> getMeasurePoints() {
        return measurePoints;
    }

    public List<MeasurePoint> getDay(Date date) {
        return measurePoints.stream().filter(measurePoint -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(measurePoint.getTimestamp()).equals(sdf.format(date));
        })
                .collect(Collectors.toList());
    }

    public boolean hasDataForDate(Date date) {
        return measurePoints.stream().filter(measurePoint -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(measurePoint.getTimestamp()).equals(sdf.format(date));
        })
                .collect(Collectors.toList())
                .size() == 24;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
