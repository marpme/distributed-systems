package shared;

import java.io.Serializable;
import java.util.Date;

public class MeasurePoint implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeasurePoint that = (MeasurePoint) o;

        if (Float.compare(that.temperature, temperature) != 0) return false;
        return timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (temperature != +0.0f ? Float.floatToIntBits(temperature) : 0);
        return result;
    }
}
