package nl.tudelft.sem.hoa.unit;

import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HoaTest {

    @Test
    void testToString() {
        Hoa beilenWest = new Hoa(new HoaId("krot123"), "Netherlands", "Beilen");

        Assertions.assertEquals(beilenWest.toString(), "Hoa {"
            + beilenWest.getId()
            + ", hoaId= "
            + "krot123"
            + ", country= "
            + "Netherlands"
            + ", city= "
            + "Beilen"
            + '}');
    }

    @Test
    void testGetIdReflexivity() {
        Hoa beilenWest = new Hoa(new HoaId("krot123"), "Netherlands", "Beilen");
        Assertions.assertEquals(beilenWest.getId(), beilenWest.getId());
    }

    @Test
    void testGetHoaId() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");
        Assertions.assertEquals(beilenWest.getHoaId(), krotWijk);
    }

    @Test
    void testGetCountry() {
        Hoa beilenWest = new Hoa(new HoaId("krot123"), "Netherlands", "Beilen");
        Assertions.assertEquals(beilenWest.getCountry(), "Netherlands");
    }

    @Test
    void testGetCity() {
        Hoa beilenWest = new Hoa(new HoaId("krot123"), "Netherlands", "Beilen");
        Assertions.assertEquals(beilenWest.getCity(), "Beilen");
    }

    @Test
    void testEquals() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");
        Hoa other = new Hoa(krotWijk, "Netherlands", "Beilen");
        Hoa third = beilenWest;
        Assertions.assertTrue(beilenWest.equals(other) && other.equals(beilenWest));
        Assertions.assertTrue(beilenWest.equals(third) && third.equals(beilenWest));
    }

    @Test
    void testEqualsFalse() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");
        Hoa krot124 = new Hoa(new HoaId("krot124"), "Netherlands", "Beilen");
        Assertions.assertFalse(beilenWest.equals(krot124));
        Hoa antilles = new Hoa(krotWijk, "Dutch Antilles", "Beilen");
        Assertions.assertFalse(beilenWest.equals(antilles));
        Hoa bielenWest = new Hoa(krotWijk, "Netherlands", "Bielen");
        Assertions.assertFalse(beilenWest.equals(bielenWest));
        Object third = null;
        Assertions.assertFalse(beilenWest.equals(third));
        Object fourth = new String("Invalid");
        Assertions.assertFalse(beilenWest.equals(fourth));
        Hoa beilenWest2 = new Hoa(krotWijk, "Netherlands", "Beilen");
        beilenWest2.setId(23);
        Assertions.assertFalse(beilenWest.equals(beilenWest2));
        Assertions.assertEquals(beilenWest2.getId(), 23);
    }

    @Test
    void testHashCode() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");
        Hoa other = new Hoa(krotWijk, "Netherlands", "Beilen");

        Assertions.assertEquals(other.hashCode(), beilenWest.hashCode());
    }
}