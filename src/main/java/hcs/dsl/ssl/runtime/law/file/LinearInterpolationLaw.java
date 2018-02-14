package hcs.dsl.ssl.runtime.law.file;

import com.udojava.evalex.Expression;
import hcs.dsl.ssl.runtime.law.Law;
import hcs.dsl.ssl.runtime.sensor.Source;

import java.math.BigDecimal;

public abstract class LinearInterpolationLaw<T extends Number> extends Law<T> {

    public static final String TIMESTAMP_VAR = "x";

    private final TimeMetadata timeMetadata;
    private final Expression expression;
    private final Restriction<T> restriction;

    public LinearInterpolationLaw(TimeMetadata timeMetadata,
                                  Expression expression,
                                  Restriction<T> restriction) {
        this.timeMetadata = timeMetadata;
        this.expression = expression;
        this.restriction = restriction;
    }

    protected BigDecimal eval(long timestamp) {
        timestamp = timeMetadata.apply(timestamp);
        BigDecimal val = expression.with(TIMESTAMP_VAR, BigDecimal.valueOf(timestamp)).eval();
        return restriction != null ? restriction.apply(val) : val;
    }
}
