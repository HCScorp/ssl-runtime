package hcs.dsl.ssl.runtime.law.file;

public class Restriction<T extends Number> {

    private final Double min;
    private final Double max;

    public Restriction(T min, T max) {
        this.min = min.doubleValue();
        this.max = max.doubleValue();
    }

    public Double apply(Double value) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        }

        return value;
    }
}
