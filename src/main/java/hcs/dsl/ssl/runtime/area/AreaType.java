package hcs.dsl.ssl.runtime.area;

import org.influxdb.InfluxDB;

import java.util.ArrayList;
import java.util.List;


public class AreaType implements Runnable {

    private final SensorGroup[] sensorGroups;
    private final String name;

    public AreaType(String name, SensorGroup... sensorGroups) {
        this.name = name;
        this.sensorGroups = sensorGroups;
    }

    public String getName() {
        return name;
    }

    public void applyOffset(long offset) {
        for (SensorGroup sg : sensorGroups) {
            sg.applyOffset(offset);
        }
    }

    public void configure(String execName, String areaInstance, InfluxDB influxDB) {
        for (SensorGroup sg : sensorGroups) {
            sg.configure(execName, areaInstance, name, influxDB);
        }
    }

    public void process(long start, long end) {
        for (SensorGroup sg : sensorGroups) {
            sg.process(start, end);
        }
    }

    @Override
    public void run() {
        List<Thread> threads = new ArrayList<>();
        for (SensorGroup sg : sensorGroups) {
            Thread t = new Thread(sg);
            threads.add(t);
            t.start();
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            // TODO log?
        }
    }
}
