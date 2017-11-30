package client.input;

import shared.MeasurePoint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
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

    /**
     * Modifies a specific measurepoint if present in current data
     * @param measurePoint the updated measurepoint
     */
    public void modifyMeasurePoint(MeasurePoint measurePoint) {
        measurePoints.removeIf(point -> point.getTimestamp().equals(measurePoint.getTimestamp()));
        measurePoints.add(measurePoint);
    }

    /**
     * Returns all measurepoints of a specific day
     * @param date the day
     * @return sorted list of measurepoints or null
     */
    public List<MeasurePoint> getDay(Date date) {
        // gathering results
        List<MeasurePoint> result = measurePoints.stream().filter(measurePoint -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    return sdf.format(measurePoint.getTimestamp()).equals(sdf.format(date));
                }).collect(Collectors.toList());
        // sorting by time
        if (result != null) {
            Collections.sort(result, Comparator.comparing(MeasurePoint::getTimestamp));
        }
        return result;
    }

    public boolean hasDataForDate(Date date) {
        return measurePoints.stream().filter(measurePoint -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(measurePoint.getTimestamp()).equals(sdf.format(date));
        })
                .collect(Collectors.toList())
                .size() == 24;
    }

    public List<MeasurePoint> getMeasurePoints() {
        return measurePoints;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
