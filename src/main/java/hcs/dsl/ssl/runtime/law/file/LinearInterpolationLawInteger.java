package hcs.dsl.ssl.runtime.law.file;

import com.udojava.evalex.Expression;

public class LinearInterpolationLawInteger extends LinearInterpolationLaw<Integer> {

    public LinearInterpolationLawInteger(Expression expression) {
        super(expression);
    }

    @Override
    public Integer produceValue(long timestamp) {
        return eval(timestamp).intValue();
    }
}
