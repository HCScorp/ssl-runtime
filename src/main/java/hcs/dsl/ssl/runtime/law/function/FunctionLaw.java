package hcs.dsl.ssl.runtime.law.function;

import com.udojava.evalex.Expression;
import hcs.dsl.ssl.runtime.law.Law;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class FunctionLaw<T extends Serializable> extends Law<T> {

    public static final String TIMESTAMP_VAR = "x";

    private final List<CondExpr<T>> condExprList;

    public FunctionLaw() {
        this.condExprList = new ArrayList<>();
    }

    protected void addCase(String condition, T value) {
        this.condExprList.add(new CondExpr<>(condition, value));
    }

    @Override
    public T produceValue(long timestamp) {
        BigDecimal bTimestamp = BigDecimal.valueOf(timestamp);
        for (CondExpr<T> ce : condExprList) {
            Expression cond = ce.getCondition().with(TIMESTAMP_VAR, bTimestamp);
            BigDecimal val = cond.eval();
            if (!BigDecimal.ZERO.equals(val)) {
                return ce.getValue();
            }
        }
        return getDefault();
    }

    protected abstract T getDefault();
}
