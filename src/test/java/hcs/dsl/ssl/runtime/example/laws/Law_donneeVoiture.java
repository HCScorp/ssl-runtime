package hcs.dsl.ssl.runtime.example.laws;


import hcs.dsl.ssl.runtime.law.file.LinearInterpolationLawInteger;
import hcs.dsl.ssl.runtime.law.file.Restriction;
import hcs.dsl.ssl.runtime.law.file.TimeMetadata;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

public class Law_donneeVoiture extends LinearInterpolationLawInteger {

    public Law_donneeVoiture() {
        super(new TimeMetadata(0, 100),
                new Restriction<>(-3, 10),
                new double[]{
                        0.0, 2.0, 4.0
                },
                new PolynomialFunction(new double[]{
                        3.0, 4.0
                }),
                new PolynomialFunction(new double[]{
                        11.0, -2.5
                })
        );
    }
}
