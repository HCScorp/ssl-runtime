package hcs.dsl.ssl.runtime.law.file;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

public class LinearInterpolationLawInteger extends LinearInterpolationLaw<Integer> {

    public LinearInterpolationLawInteger(TimeMetadata timeMetadata,
                                        Restriction<Integer> restriction,
                                        double[] knots,
                                        PolynomialFunction... polynomials){
        super(timeMetadata, restriction, knots, polynomials);
    }

    @Override
    public Integer produceValue(long timestamp) {
        return eval(timestamp).intValue();
    }
}
