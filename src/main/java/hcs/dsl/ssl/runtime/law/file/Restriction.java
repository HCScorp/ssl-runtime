package hcs.dsl.ssl.runtime.law.file;

import java.math.BigDecimal;

public class Restriction<T extends Number> {

    private final Double min;
    private final Double max;

    public Restriction(T min, T max) {
        this.min = min.doubleValue();
        this.max = max.doubleValue();
    }

    public BigDecimal apply(BigDecimal value) {
        double val = value.doubleValue();
        if (val > max) {
            return BigDecimal.valueOf(max);
        } else if (val < min) {
            return BigDecimal.valueOf(min);
        }

        return value;
    }
}
