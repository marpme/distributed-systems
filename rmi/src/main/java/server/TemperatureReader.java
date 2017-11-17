package server;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.time.DateUtils;
import shared.MeasurePoint;
import sun.awt.image.ImageWatched;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemperatureReader {

    private static final String PATH = "/Users/jankulose/IdeaProjects/distributed-systems/rmi/src/main/resources/temperatures.csv";
    private static TemperatureReader instance;
    private List<MeasurePoint> measurePoints = null;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private TemperatureReader() {
        File data = new File(PATH);
        String s = "";
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            String line;
            while ((line = br.readLine()) != null) {
                s += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("");


//        try {
//            temperatures = new CsvToBeanBuilder(new FileReader(PATH))
//                    .withType(MeasurePoint.class)
//                    .withSeparator(';')
//                    .withSkipLines(1)
//                    .build()
//                    .parse();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    static synchronized TemperatureReader getInstance() {
        if (TemperatureReader.instance == null) {
            TemperatureReader.instance = new TemperatureReader();
        }

        return TemperatureReader.instance;
    }


    Optional<MeasurePoint> receiveTemperatureForDate(Date date) {
        return measurePoints.stream().filter(measurePoint -> measurePoint.getTimestamp().equals(date)).findFirst();
    }

    List<MeasurePoint> receiveMeasurePoints(Date date) {
//        try {
//            Optional<TemperatureHistory> optionalTemps = temperatures.stream().filter(temp -> temp.getDate().equals(date)).findFirst();
//            if (optionalTemps.isPresent()) {
//
//                TemperatureHistory temperature = optionalTemps.get();
//                List<MeasurePoint> measurements = new LinkedList<>();
//
//                for (int i = 0; i < temperature.getWeatherData().size(); i++) {
//                    MeasurePoint measure = new MeasurePoint(
//                            DateUtils.addHours(temperature.getDate(), i),
//                            Float.parseFloat(temperature.getWeatherData().get(i))
//                    );
//
//                    measurements.add(measure);
//                }
//
//                return measurements;
//            } else {
//                return new LinkedList<>();
//            }
//        } catch (NumberFormatException e) {
//            return new LinkedList<>();
//        }
        return measurePoints;
    }
}
