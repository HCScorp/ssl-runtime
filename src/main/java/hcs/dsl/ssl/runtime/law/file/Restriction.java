package hcs.dsl.ssl.runtime.law.file;

public class Restriction<T extends Number> {

    public T min;
    public T max;

    public Restriction(T min, T max) {
        this.min = min;
        this.max = max;
    }
}
