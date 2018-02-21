package hcs.dsl.ssl.runtime.law.file;

import com.udojava.evalex.Expression;

public class LinearInterpolationLawInteger extends LinearInterpolationLaw<Integer> {

    public LinearInterpolationLawInteger(TimeMetadata timeMetadata,
                                        Expression expression,
                                        Restriction<Integer> restriction) {
        super(timeMetadata, expression, restriction);
    }

    @Override
    public Integer produceValue(long timestamp) {
        return eval(timestamp).intValue();
    }
}
