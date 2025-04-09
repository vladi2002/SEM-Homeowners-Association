package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.HoaId;
import org.junit.jupiter.api.Test;

public class HoaIdTest {
    @Test
    public void constructorTest() {
        HoaId org = new HoaId("someone");
        assertNotNull(org);
    }

    @Test
    public void equalsTest() {
        final HoaId hoa = new HoaId("something");
        final HoaId hoa1 = new HoaId("something");
        final HoaId hoa2 = new HoaId("somhing");
        final Object o1 = new HoaId("something");
        final Object o2 = new Object();
        final Object o = null;
        assertEquals(hoa, o1);
        assertEquals(hoa, hoa1);
        assertNotEquals(hoa, o);
        assertNotEquals(hoa, o2);
        assertNotEquals(hoa, hoa2);
    }

    @Test
    public void hashCodeTest() {
        final HoaId hoa = new HoaId("something");
        final HoaId hoa1 = new HoaId("something");
        assertEquals(hoa.hashCode(), hoa1.hashCode());
    }

    @Test
    public void toStringTest1() {
        HoaId org = new HoaId("someone");
        assertEquals(org.toString(), "someone");
    }

    @Test
    public void toStringTest2() {
        HoaId org = new HoaId("somone");
        assertNotEquals(org.toString(), "someone");
    }
}
