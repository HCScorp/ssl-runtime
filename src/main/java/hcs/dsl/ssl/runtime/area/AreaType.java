package hcs.dsl.ssl.runtime.area;

import org.influxdb.InfluxDB;

import java.time.LocalDateTime;
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

    public void applyOffset(long offset, LocalDateTime now) {
        for (SensorGroup sg : sensorGroups) {
            sg.applyOffset(offset, now);
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
            System.out.println("starting sensor group " + sg.getSensorName() + ":" + sg.getQuantity());
            Thread t = new Thread(sg);
            threads.add(t);
            t.start();
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            // TODO log ?
        }
    }
}
