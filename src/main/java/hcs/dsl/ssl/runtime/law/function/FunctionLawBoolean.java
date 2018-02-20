package hcs.dsl.ssl.runtime.law.function;

import com.udojava.evalex.Expression;
import java.math.BigDecimal;

public abstract class FunctionLawBoolean extends FunctionLaw<Boolean> {

    @Override
    public Boolean produceValue(long timestamp) {
        BigDecimal bTimestamp = BigDecimal.valueOf(timestamp);
        for (CondExpr<Boolean> ce : condExprList) {
            Expression cond = ce.getCondition().with(TIMESTAMP_VAR, bTimestamp);
            BigDecimal val = cond.eval();
            if (!BigDecimal.ZERO.equals(val)) {
                return ce.getValue();
            }
        }
        return false;
    }
}