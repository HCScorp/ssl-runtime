package hcs.dsl.ssl.runtime.law.function;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class FunctionLawExpr extends FunctionLaw<Double> {

    private class CondExpr {
        private Expression condition;
        private Expression expression;

        CondExpr(String condition, String expression) {
            this.condition = new Expression(condition);
            this.expression = new Expression(expression);
        }
    }

    private final List<CondExpr> condExprList;

    public FunctionLawExpr() {
        this.condExprList = new ArrayList<>();
    }

    protected void addCase(String condition, String expression) {
        this.condExprList.add(new CondExpr(condition, expression));
    }

    @Override
    public Double produceValue(long timestamp) {
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

    private Double eval(BigDecimal val) {
        if (val == null) {
            return 0.0;
        }

        return val.doubleValue();
    }
}
