package hcs.dsl.ssl.runtime.sensor;

import hcs.dsl.ssl.runtime.law.file.TimeMetadataOwner;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Sensor<T extends Serializable> implements Runnable {

    private final String name;
    private final Source<T> source;
    private final long periodMs;

    private String areaInstance;
    private String areaType;
    private String exec;

    private long offset = 0;

    private InfluxDB influxDB;
    private final String id;

    public Sensor(String name, Source<T> source, long periodMs) {
        this.name = name;
        this.source = source;
        this.periodMs = periodMs;
        this.id = Integer.toString(java.lang.System.identityHashCode(this));
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setOffset(long offset, LocalDateTime now) {
        if (source instanceof TimeMetadataOwner) { // quick and dirty but isolated to Sensor class
            LocalDateTime start =
                    LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(
                                    ((TimeMetadataOwner) source).getTimeMetadata().getMin()),
                            TimeZone.getDefault().toZoneId());
            setOffset(ChronoUnit.MILLIS.between(now, start) / 1000);
        } else {
            setOffset(offset);
        }
    }


    public void configure(String execName, String areaInstance, String areaType, InfluxDB influxDB) {
        this.exec = execName;
        this.areaInstance = areaInstance;
        this.areaType = areaType;
        this.influxDB = influxDB;
    }

    public T produceValue(long timestamp) {
        return source.produceValue(timestamp + offset);
    }

    public String getName() {
        return name;
    }

    public long getPeriodMs() {
        return periodMs;
    }

    public Source<T> getSource() {
        return source;
    }

    @Override
    public void run() {
        process(TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }

    public void process(long timestamp) {
        T val = produceValue(timestamp);

        Point.Builder builder = Point.measurement(name)
                .tag("areaType", areaType)
                .tag("areaInstance", areaInstance)
                .tag("app", exec)
                .tag("id", id)
                .time(timestamp, TimeUnit.SECONDS);

        // quick and dirty but isolated to Sensor class
        if (val instanceof Number) {
            builder.addField("value", (Number) val);
        } else if (val instanceof Boolean) {
            builder.addField("value", (Boolean) val);
        } else if (val instanceof String) {
            builder.addField("value", (String) val);
        }

        influxDB.write(builder.build());
    }
}
