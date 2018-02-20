package hcs.dsl.ssl.runtime.law.function;

import com.udojava.evalex.Expression;

import java.io.Serializable;

public class CondExpr<T extends Serializable> {

    private final Expression condition;
    private final T value;

    public CondExpr(String condition, T value) {
        this.condition = new Expression(condition);
        this.value = value;
    }

    public Expression getCondition() {
        return condition;
    }

    public T getValue() {
        return value;
    }
}
