package hcs.dsl.ssl.runtime.example.laws;


import com.udojava.evalex.Expression;
import hcs.dsl.ssl.runtime.law.file.LinearInterpolationLawInteger;

public class Law_donneeVoiture extends LinearInterpolationLawInteger { // Type defined by the value of the file

    public Law_donneeVoiture() {
        super(new Expression("a0 + a1*x^2 + a2*x^3 + a3*x^4 + a4*x^5")
                .with("a0", "2")
                .with("a1", "0.0065")
                .with("a2", "0.046")
                .with("a3", "0.2319")
                .with("a4", "-0.0205")
        );
    }
}