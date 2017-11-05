package server;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class TemperatureReader {

    private static final String PATH = "C:\\Users\\Kyon\\Desktop\\distributed-systems\\temperature\\src\\main\\resources\\temperatures.csv";
    private static TemperatureReader instance;

    private TemperatureReader() {
    }

    static synchronized TemperatureReader getInstance() {
        if (TemperatureReader.instance == null) {
            TemperatureReader.instance = new TemperatureReader();
        }

        return TemperatureReader.instance;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    List receiveTemperatureForDate(String Date) {
        try {
            return new CsvToBeanBuilder(new FileReader(PATH))
                    .withType(TemperatureHistory.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
