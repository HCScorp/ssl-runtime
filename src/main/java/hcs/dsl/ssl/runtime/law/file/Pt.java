package hcs.dsl.ssl.runtime.law.file;

public class Pt<T> {
    public long timestamp;
    public T value;

    public Pt(long timestamp, T value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}
