package nl.tudelft.sem.hoa.unit;

import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HoaIdTest {

    @Test
    void testToString() {
        HoaId krotWijk123 = new HoaId("krottenWijk123");
        Assertions.assertEquals(krotWijk123.toString(), "krottenWijk123");
    }

    @Test
    void testEquals() {
        HoaId krotWijk123 = new HoaId("krottenWijk123");
        HoaId o = new HoaId("krottenWijk123");
        HoaId same = krotWijk123;
        Assertions.assertTrue(krotWijk123.equals(o) && o.equals(krotWijk123));
        Assertions.assertTrue(krotWijk123.equals(same) && same.equals(krotWijk123));
    }

    @Test
    void testEqualsFalse() {
        HoaId krotWijk123 = new HoaId("krottenWijk123");
        HoaId o = new HoaId("krotteWijk123");
        Object none = null;
        Object s = new String("Other class");
        HoaId wrongKrotWijk123 = new HoaId("krotenWijk123");
        Assertions.assertFalse(krotWijk123.equals(o));
        Assertions.assertFalse(krotWijk123.equals(none));
        Assertions.assertFalse(krotWijk123.equals(s));
        Assertions.assertFalse(krotWijk123.equals(wrongKrotWijk123));
    }

    @Test
    void testHashCode() {
        HoaId krotWijk123 = new HoaId("krottenWijk123");
        HoaId o = new HoaId("krottenWijk123");
        Assertions.assertEquals(krotWijk123.hashCode(), o.hashCode());
    }
}