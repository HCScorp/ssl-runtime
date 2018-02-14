package hcs.dsl.ssl.runtime.sensor;

import java.io.Serializable;

public abstract class Source<T extends Serializable> {
    public abstract T produceValue(long timestamp);
}