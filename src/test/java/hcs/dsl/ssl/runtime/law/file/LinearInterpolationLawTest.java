package hcs.dsl.ssl.runtime.law.file;

import org.junit.Test;
import hcs.dsl.ssl.runtime.example.laws.Law_donneeVoiture;

import static org.junit.Assert.assertEquals;

public class LinearInterpolationLawTest {

    @Test
    public void donneeVoitureTest() {
        Law_donneeVoiture l = new Law_donneeVoiture();
        assertEquals(2, l.produceValue(0).intValue());
        assertEquals(0, l.produceValue(9).intValue());
        assertEquals(10, l.produceValue(-12).intValue());
    }
}
