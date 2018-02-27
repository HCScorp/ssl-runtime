package hcs.dsl.ssl.runtime.area;

import hcs.dsl.ssl.runtime.noise.Noise;
import hcs.dsl.ssl.runtime.sensor.NoisableSensor;
import hcs.dsl.ssl.runtime.sensor.Sensor;
import hcs.dsl.ssl.runtime.law.file.Pt;
import hcs.dsl.ssl.runtime.law.file.RawFileLaw;
import hcs.dsl.ssl.runtime.sensor.Source;
import org.influxdb.InfluxDB;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SensorGroup implements Runnable {

    private final List<Sensor> sensors = new ArrayList<>();
    private final Sensor firstSensor;
    private final String sensorName;
    private final Source sensorSource;

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

        firstSensor = sensors.get(0);
        sensorName = firstSensor.getName();
        sensorSource = firstSensor.getSource();
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

        firstSensor = sensors.get(0);
        sensorName = firstSensor.getName();
        sensorSource = firstSensor.getSource();
    }

    public String getSensorName() {
        return sensorName;
    }

    public int getQuantity() {
        return sensors == null ? 0 : sensors.size();
    }

    public void applyOffset(long offset, LocalDateTime now) {
        sensors.forEach(s -> s.setOffset(offset, now));
    }

    public void configure(String execName, String areaInstance, String areaType, InfluxDB influxDB) {
        sensors.forEach(s -> s.configure(execName, areaInstance, areaType, influxDB));
    }

    public void process(long start, long end) {
        start = TimeUnit.MILLISECONDS.convert(start, TimeUnit.SECONDS);
        end = TimeUnit.MILLISECONDS.convert(end, TimeUnit.SECONDS);

        // quick and dirty but isolated to SensorGroup class
        if (sensorSource instanceof RawFileLaw) {
            processSourceFileRaw();
            return;
        }

        long period = firstSensor.getPeriodMs();

        System.out.println("start feeding db for sensors " + sensorName + " (period: " + period + "ms)");

        for (long i = start; i < end; i += period) {
            for (Sensor s : sensors) {
                s.process(TimeUnit.SECONDS.convert(i, TimeUnit.MILLISECONDS));
            }
        }
    }

    private void processSourceFileRaw() {
        System.out.println("start feeding InfluxDB for sensors " + sensorName + "");

        RawFileLaw rfl = (RawFileLaw) sensorSource;
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
        // quick and dirty but isolated to SensorGroup class
        if (sensorSource instanceof RawFileLaw) {
            processSourceFileRawRealtime();
        } else {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
            sensors.forEach(s -> executor.scheduleAtFixedRate(s, 0, s.getPeriodMs(), TimeUnit.MILLISECONDS));
        }
    }
}
