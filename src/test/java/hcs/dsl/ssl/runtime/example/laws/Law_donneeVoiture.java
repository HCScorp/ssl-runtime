package hcs.dsl.ssl.runtime.example.laws;


import com.udojava.evalex.Expression;
import hcs.dsl.ssl.runtime.law.file.LinearInterpolationLawInteger;
import hcs.dsl.ssl.runtime.law.file.Restriction;
import hcs.dsl.ssl.runtime.law.file.TimeMetadata;

public class Law_donneeVoiture extends LinearInterpolationLawInteger {

    public Law_donneeVoiture() {
        super(new TimeMetadata(0, 100),
                new Expression("a0 + a1*x^2 + a2*x^3 + a3*x^4 + a4*x^5")
                .with("a0", "2")
                .with("a1", "-0.0065")
                .with("a2", "-0.046")
                .with("a3", "-0.2319")
                .with("a4", "-0.0205"), new Restriction<>(0, 10)
        );
    }
}
