package hcs.dsl.ssl.runtime.law.function;

import hcs.dsl.ssl.runtime.law.Law;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class FunctionLaw<T extends Serializable> extends Law<T> {

    protected static final String TIMESTAMP_VAR = "x";

    protected final List<CondExpr<T>> condExprList;

    public FunctionLaw() {
        this.condExprList = new ArrayList<>();
    }

    protected void addCase(String condition, T value) {
        this.condExprList.add(new CondExpr<>(condition, value));
    }
}
