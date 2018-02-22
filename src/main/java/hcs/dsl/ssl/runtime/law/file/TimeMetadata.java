package hcs.dsl.ssl.runtime.law.file;

public class TimeMetadata {

    private final long min;
    private final long max;

    private boolean interpolate = false;

    public TimeMetadata(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public long apply(long timestamp) {
        return (Math.abs(timestamp-min) % (max - min)) + (interpolate ? 0 : min);
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public void setInterpolate() {
        this.interpolate = true;
    }
}
