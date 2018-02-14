package hcs.dsl.ssl.runtime.law.file;

import com.udojava.evalex.Expression;

public class LinearInterpolationLawDouble extends LinearInterpolationLaw<Double> {

    public LinearInterpolationLawDouble(TimeMetadata timeMetadata,
                                        Expression expression,
                                        Restriction<Double> restriction) {
        super(timeMetadata, expression, restriction);
    }

    @Override
    public Double produceValue(long timestamp) {
        return eval(timestamp).doubleValue();
    }
}
