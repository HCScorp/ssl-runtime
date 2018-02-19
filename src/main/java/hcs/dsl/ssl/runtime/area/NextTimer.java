package hcs.dsl.ssl.runtime.area;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NextTimer implements Runnable {

    private final Timer timer;
    private final TimerTask task;

    public NextTimer(final Supplier<Long> producer,
                     final Consumer<Long> consumer,
                     final Supplier<Long> wait) {
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                consumer.accept(producer.get());
                timer.schedule(task, wait.get());
            }
        };
    }

    @Override
    public void run() {
        task.run();
    }

    public void cancel() {
        timer.cancel();
        timer.purge();
    }
}
