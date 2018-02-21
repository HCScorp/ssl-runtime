package hcs.dsl.ssl.runtime.area;

import hcs.dsl.ssl.runtime.law.file.TimeMetadataOwner;
import hcs.dsl.ssl.runtime.noise.Noise;
import hcs.dsl.ssl.runtime.sensor.NoisableSensor;
import hcs.dsl.ssl.runtime.sensor.Sensor;
import hcs.dsl.ssl.runtime.law.file.Pt;
import hcs.dsl.ssl.runtime.law.file.RawFileLaw;
import org.influxdb.InfluxDB;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SensorGroup implements Runnable {

    private List<Sensor> sensors = new ArrayList<>();

    public <T extends Number> SensorGroup(int number,
                                          Class<? extends NoisableSensor<T>> sensorClass,
                                          Noise<T> noiseOverride) {
        for (int i = 0; i < number; i++) {
            NoisableSensor s;
            try {
                Constructor<? extends NoisableSensor<T>> constructor = sensorClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                s = constructor.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }

            if (noiseOverride != null) {
                s.setNoise(noiseOverride);
            }

            sensors.add(s);
        }
    }

    public SensorGroup(int number, Class<? extends Sensor> sensorClass) {
        for (int i = 0; i < number; i++) {
            Sensor s;
            try {
                Constructor<? extends Sensor> constructor = sensorClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                s = constructor.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }

            sensors.add(s);
        }
    }

    public String getSensorName() {
        if (sensors == null || sensors.isEmpty()) {
            return "";
        }

        return sensors.get(0).getName();
    }

    public int getQuantity() {
        if (sensors == null) {
            return 0;
        }

        return sensors.size();
    }

    public void applyOffset(long offset, LocalDateTime now) {
        for (Sensor s : sensors) {
            if (s.getSource() instanceof TimeMetadataOwner) {
                LocalDateTime start =
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(
                                        ((TimeMetadataOwner) s.getSource()).getTimeMetadata().getMin()),
                                TimeZone.getDefault().toZoneId());
                s.setOffset(ChronoUnit.MILLIS.between(now, start) / 1000);
            } else {
                s.setOffset(offset);
            }
        }
    }

    public void configure(String execName, String areaInstance, String areaType, InfluxDB influxDB) {
        for (Sensor s : sensors) {
            s.configure(execName, areaInstance, areaType, influxDB);
        }
    }

    public void process(long start, long end) {
        start = TimeUnit.MILLISECONDS.convert(start, TimeUnit.SECONDS);
        end = TimeUnit.MILLISECONDS.convert(end, TimeUnit.SECONDS);

        if (sensors.get(0).getSource() instanceof RawFileLaw) {
            processSourceFileRaw();
            return;
        }

        long period = sensors.get(0).getPeriodMs();

        System.out.println("start feeding db for sensors " + sensors.get(0).getName() + " (period: " + period + "ms)");

        for (long i = start; i < end; i += period) {
            for (Sensor s : sensors) {
                s.process(TimeUnit.SECONDS.convert(i, TimeUnit.MILLISECONDS));
            }
        }
    }

    private void processSourceFileRaw() {
        System.out.println("Start feeding InfluxDB for sensors " + sensors.get(0).getName() + "");

        RawFileLaw rfl = (RawFileLaw) sensors.get(0).getSource();
        for (Sensor s : sensors) {
            for (Pt pt : rfl.values) {
                s.process(pt.timestamp);
            }
        }
    }

    private void processSourceFileRawRealtime() {
        for (Sensor s : sensors) {
            RawFileLaw rfl = (RawFileLaw) s.getSource();
            new Thread(
                    new NextTimer(rfl::currTimestamp, s::process, rfl::nextWait)
            ).start();
        }
    }

    @Override
    public void run() {
        if (sensors.get(0).getSource() instanceof RawFileLaw) {
            processSourceFileRawRealtime();
        } else {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
            for (Sensor s : sensors) {
                executor.scheduleAtFixedRate(s, 0, s.getPeriodMs(), TimeUnit.MILLISECONDS);
            }
        }
    }
}
