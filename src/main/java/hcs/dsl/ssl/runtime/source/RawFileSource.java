package hcs.dsl.ssl.runtime.source;

import java.io.Serializable;

public class RawFileSource<T extends Serializable> extends Source<T> {

    public final Pt<T>[] values;
    private int i = 0;

    public RawFileSource(Pt<T>... values) {
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
