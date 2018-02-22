package hcs.dsl.ssl.runtime.law.file;

import hcs.dsl.ssl.runtime.law.Law;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public abstract class LinearInterpolationLaw<T extends Number> extends Law<T>  implements TimeMetadataOwner {

    private final TimeMetadata timeMetadata;
    private final PolynomialSplineFunction function;
    private final Restriction<T> restriction;

    public LinearInterpolationLaw(TimeMetadata timeMetadata,
                                  Restriction<T> restriction,
                                  double[] knots,
                                  PolynomialFunction... polynomials) {
        this.timeMetadata = timeMetadata;
        this.timeMetadata.setInterpolate();
        this.function = new PolynomialSplineFunction(knots, polynomials);
        this.restriction = restriction;
    }

    protected Double eval(long timestamp) {
        timestamp = timeMetadata.apply(timestamp);
        double val = function.value((double) timestamp);
        return restriction != null ? restriction.apply(val) : val;
    }

    @Override
    public TimeMetadata getTimeMetadata() {
        return timeMetadata;
    }
}
