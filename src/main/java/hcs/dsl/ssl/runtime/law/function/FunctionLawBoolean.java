package hcs.dsl.ssl.runtime.law.function;

public abstract class FunctionLawBoolean extends FunctionLaw<Boolean> {

    @Override
    protected Boolean getDefault() {
        return false;
    }
}