package hcs.dsl.ssl.runtime.law.file;

import hcs.dsl.ssl.runtime.law.Law;

import java.io.Serializable;

public class RawFileLaw<T extends Serializable> extends Law<T> implements TimeMetadataOwner {

    private final TimeMetadata timeMetadata;

    public final Pt<T>[] values;
    private int i = 0;

    public RawFileLaw(TimeMetadata timeMetadata, Pt<T>... values) {
        this.timeMetadata = timeMetadata;
        this.values = values;
    }

    public TimeMetadata getTimeMetadata() {
        return timeMetadata;
    }

    @Override
    public T produceValue(long timestamp) {
        if (i >= values.length) {
            i = 0;
        }
        return values[i++].value;
    }

    public long nextWait() {
        if (i >= values.length - 1) {
            return values[values.length-1].timestamp - values[values.length-2].timestamp;
        }

        return values[i+1].timestamp - values[i].timestamp;
    }

    public long currTimestamp() {
        return values[i].timestamp;
    }
}
