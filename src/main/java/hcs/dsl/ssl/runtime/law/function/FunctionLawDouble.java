package hcs.dsl.ssl.runtime.law.function;

import com.udojava.evalex.Expression;
import java.math.BigDecimal;

public abstract class FunctionLawDouble extends FunctionLaw<Double> {

    @Override
    public Double produceValue(long timestamp) {
        BigDecimal bTimestamp = BigDecimal.valueOf(timestamp);
        for (CondExpr<Double> ce : condExprList) {
            Expression cond = ce.getCondition().with(TIMESTAMP_VAR, bTimestamp);
            BigDecimal val = cond.eval();
            if (!BigDecimal.ZERO.equals(val)) {
                return ce.getValue();
            }
        }
        return 0.0;
    }
}
