package hcs.dsl.ssl.runtime.law.file;

public class TimeMetadata {

    private final long min;
    private final long max;

    public TimeMetadata(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public long apply(long timestamp) {
        return (timestamp % (max - min)) + min;
    }
}
