package hcs.dsl.ssl.runtime.law.file;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

public class LinearInterpolationLawDouble extends LinearInterpolationLaw<Double> {

    public LinearInterpolationLawDouble(TimeMetadata timeMetadata,
                                        Restriction<Double> restriction,
                                        double[] knots,
                                        PolynomialFunction... polynomials){
        super(timeMetadata, restriction, knots, polynomials);
    }

    @Override
    public Double produceValue(long timestamp) {
        return eval(timestamp);
    }
}
