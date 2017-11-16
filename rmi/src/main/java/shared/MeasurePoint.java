package shared;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import server.ConvertListToTemperatures;

import java.util.Date;

public class MeasurePoint {

    Date timestamp;

    float temperature;

    public MeasurePoint(Date timestamp, float temperature) {
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
