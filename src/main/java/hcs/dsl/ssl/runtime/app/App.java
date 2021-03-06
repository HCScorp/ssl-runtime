package hcs.dsl.ssl.runtime.app;

import hcs.dsl.ssl.runtime.area.AreaInstance;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class App implements Runnable {

    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final AreaInstance[] areaInstances;
    private final Config conf;
    private final String name;
    private InfluxDB influxDB;

    public App(String name, Config conf, AreaInstance... areaInstances) {
        this.name = name;
        this.areaInstances = areaInstances;
        this.conf = conf;
    }

    @Override
    public void run() {
        String address = System.getenv("SSL_INFLUXDB_ADDRESS");
        String db = System.getenv("SSL_INFLUXDB_DB");

        System.out.println("connecting to influxdb " + address);
        influxDB = InfluxDBFactory.connect(
                address,
                System.getenv("SSL_INFLUXDB_USER"),
                System.getenv("SSL_INFLUXDB_PWD"));

        Pong pong = influxDB.ping();
        if (pong == null) {
            throw new IllegalStateException("failed to connect to influxdb" + address);
        }

        System.out.println("successfully connected to influxdb " + address + " on db " + db);

        System.out.println("setting db '" + db + "'");
        if (!influxDB.databaseExists(db)) {
            System.out.println("db not found, creating db '" + db + "'");
            influxDB.createDatabase(db);
        }
        influxDB.setDatabase(db);

        System.out.println("successfully set db '" + db + "'");

        for (AreaInstance ai : areaInstances) {
            ai.configure(name, influxDB);
        }

        if (conf.isRealtime()) {
            System.out.println("starting simulation in realtime mode");
            runRealtime();
        } else {
            System.out.println("starting simulation in replay mode");
            runReplay();
        }

        influxDB.close();
    }

    private void runRealtime() {
        influxDB.disableBatch();

        applyOffset(conf.getOffset());

        List<Thread> threads = new ArrayList<>();
        for (AreaInstance ai : areaInstances) {
            System.out.println("starting area " + ai.getAreaType().getName() + ":" + ai.getName() + "");
            Thread t = new Thread(ai);
            threads.add(t);
            t.start();
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException ignored) {}

        try {
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {}
    }

    private void runReplay() {
        applyOffset(conf.getStart());

        influxDB.enableBatch(1000, 2, TimeUnit.SECONDS);

        LocalDateTime start = LocalDateTime.parse(conf.getStart(), DTF);
        LocalDateTime end = LocalDateTime.parse(conf.getEnd(), DTF);

        long startTs = start.atZone(ZoneId.systemDefault()).toEpochSecond();
        long endTs = end.atZone(ZoneId.systemDefault()).toEpochSecond();

        for (AreaInstance ai : areaInstances) {
            System.out.println("generating data for area " + ai.getAreaType().getName() + ":" + ai.getName());
            ai.process(startTs, endTs);
        }
    }

    private void applyOffset(String offsetStr) {
        if (offsetStr != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime offDate = LocalDateTime.parse(offsetStr, DTF);
            long offset = ChronoUnit.MILLIS.between(now, offDate) / 1000;
            for (AreaInstance ai : areaInstances) {
                ai.applyOffset(offset, now);
            }
        }
    }
}
