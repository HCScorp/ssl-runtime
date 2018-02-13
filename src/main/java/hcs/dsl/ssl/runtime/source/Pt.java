package hcs.dsl.ssl.runtime.source;

public class Pt<T> {
    public long timestamp;
    public T value;

    public Pt(long timestamp, T value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}
