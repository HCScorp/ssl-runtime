package hcs.dsl.ssl.runtime.law.function;

public abstract class FunctionLawInteger extends FunctionLaw<Integer> {

    @Override
    protected Integer getDefault() {
        return 0;
    }
}