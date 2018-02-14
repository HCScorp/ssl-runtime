package hcs.dsl.ssl.runtime.law.function;

import com.udojava.evalex.Expression;
import hcs.dsl.ssl.runtime.law.Law;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class FunctionLaw<T extends Serializable> extends Law<T> {

    private static final String TIMESTAMP_VAR = "x";

    private class CondExpr {
        private Expression condition;
        private Expression expression;

        CondExpr(String condition, String expression) {
            this.condition = new Expression(condition);
            this.expression = new Expression(expression);
        }
    }

    private final List<CondExpr> condExprList;

    public FunctionLaw() {
        this.condExprList = new ArrayList<>();
    }

    protected void addCase(String condition, String expression) {
        this.condExprList.add(new CondExpr(condition, expression));
    }

    @Override
    public T produceValue(long timestamp) {
        BigDecimal bTimestamp = BigDecimal.valueOf(timestamp);
        for (CondExpr ce : condExprList) {
            Expression cond = ce.condition.with(TIMESTAMP_VAR, bTimestamp);
            BigDecimal val = cond.eval();
            if (!BigDecimal.ZERO.equals(val)) {
                Expression expr = ce.expression.with(TIMESTAMP_VAR, bTimestamp);
                return eval(expr.eval());
            }
        }
        return eval(BigDecimal.ZERO);
    }

    protected abstract T eval(BigDecimal val);
}
