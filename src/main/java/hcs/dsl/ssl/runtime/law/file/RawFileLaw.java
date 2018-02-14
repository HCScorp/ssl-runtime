package hcs.dsl.ssl.runtime.law.file;

import hcs.dsl.ssl.runtime.law.Law;

import java.io.Serializable;

public class RawFileLaw<T extends Serializable> extends Law<T> {

    public final Pt<T>[] values;
    private int i = 0;

    public RawFileLaw(Pt<T>... values) {
        this.values = values;
    }

    @Override
    public T produceValue(long timestamp) {
        if (i >= values.length) {
            return null;
        }
        return values[i++].value;
    }
}