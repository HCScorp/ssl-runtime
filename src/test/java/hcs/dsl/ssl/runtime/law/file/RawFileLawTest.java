package hcs.dsl.ssl.runtime.law.file;

import hcs.dsl.ssl.runtime.example.laws.Law_donneePersonneBU;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RawFileLawTest {
    @Test
    public void donneePersonneBUTest() {
        Law_donneePersonneBU l = new Law_donneePersonneBU();
        assertEquals(0, l.produceValue(0).intValue());
        assertEquals(1, l.produceValue(0).intValue());
        assertEquals(2, l.produceValue(0).intValue());
        assertEquals(5, l.produceValue(0).intValue());
    }
}
