package hcs.dsl.ssl.runtime.law.file;

import com.udojava.evalex.Expression;

public class LinearInterpolationLawDouble extends LinearInterpolationLaw<Double> {

    public LinearInterpolationLawDouble(Expression expression) {
        super(expression);
    }

    @Override
    public Double produceValue(long timestamp) {
        return eval(timestamp).doubleValue();
    }
}
