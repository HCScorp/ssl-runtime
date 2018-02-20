package hcs.dsl.ssl.runtime.law.function;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

public abstract class FunctionLawString extends FunctionLaw<String> {

    @Override
    public String produceValue(long timestamp) {
        BigDecimal bTimestamp = BigDecimal.valueOf(timestamp);
        for (CondExpr<String> ce : condExprList) {
            Expression cond = ce.getCondition().with(TIMESTAMP_VAR, bTimestamp);
            BigDecimal val = cond.eval();
            if (!BigDecimal.ZERO.equals(val)) {
                return ce.getValue();
            }
        }
        return "";
    }
}