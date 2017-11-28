package server;

import shared.MeasurePoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemperatureReader {

    private static final String PATH = "C:\\Users\\Kyon\\Desktop\\distributed-systems\\rmi\\src\\main\\resources\\temperatures.csv";
    private static TemperatureReader instance;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private List<MeasurePoint> measurePoints = new LinkedList<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    private TemperatureReader() {
        File csvFile = new File(PATH);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                try {
                    measurePoints.add(this.readMeasurePointFromLine(line));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static synchronized TemperatureReader getInstance() {
        if (TemperatureReader.instance == null) {
            TemperatureReader.instance = new TemperatureReader();
        }

        return TemperatureReader.instance;
    }


    private MeasurePoint readMeasurePointFromLine(String line) throws ParseException {
        String[] data = line.split(";");
        if (data.length != 5) {
            throw new ParseException("Invalid format detected", 0);
        } else {
            Date date = sdf.parse(data[0] + "." + data[1] + "." + data[2] + " " + data[3] + ":00");
            return new MeasurePoint(date, Float.parseFloat(data[4]));
        }
    }

    Optional<MeasurePoint> receiveTemperatureForDate(Date date) {
        return measurePoints.stream().filter(measurePoint -> measurePoint.getTimestamp().equals(date)).findFirst();
    }

    List<MeasurePoint> receiveMeasurePoints(Date date) {
        return measurePoints.stream().filter(m -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(m.getTimestamp()).equals(sdf.format(date));
        }).collect(Collectors.toList());
    }
}
