package nl.tudelft.sem.hoa.unit;

import nl.tudelft.sem.hoa.domain.hoa.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class AddressTest {

    @Test
    void getStreet() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Assertions.assertEquals(molenstraat1Assen.getStreet(), "Molenstraat");
    }

    @Test
    void getCity() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Assertions.assertEquals(molenstraat1Assen.getCity(), "Assen");
    }

    @Test
    void getHouseNumber() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Assertions.assertEquals(molenstraat1Assen.getHouseNumber(), 1);
    }

    @Test
    void getCountry() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Assertions.assertEquals(molenstraat1Assen.getCountry(), "Netherlands");
    }

    @Test
    void getPostalCode() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Assertions.assertEquals(molenstraat1Assen.getPostalCode(), "0592GG");
    }

    @Test
    void testEquals() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Address sameAddress = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Address a3 = molenstraat1Assen;
        Assertions.assertTrue(molenstraat1Assen.equals(sameAddress) && sameAddress.equals(molenstraat1Assen));
        Assertions.assertTrue(molenstraat1Assen.equals(a3) && a3.equals(molenstraat1Assen));
        Object o = null;
        Object o1 = new String("dojkfs");
        Assertions.assertFalse(molenstraat1Assen.equals(o));
        Assertions.assertFalse(molenstraat1Assen.equals(o1));
    }

    @Test
    void testEquals2() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Address sameAddress1 = new Address("Netherlands", "Asen", "Molenstraat", 1, "0592GG");
        Assertions.assertFalse(molenstraat1Assen.equals(sameAddress1));
        Address sameAddress2 = new Address("Neterlands", "Assen", "Molenstraat", 1, "0592GG");
        Assertions.assertFalse(molenstraat1Assen.equals(sameAddress2));
        Address sameAddress3 = new Address("Netherlands", "Assen", "Moenstraat", 1, "0592GG");
        Assertions.assertFalse(molenstraat1Assen.equals(sameAddress3));
        Address sameAddress4 = new Address("Netherlands", "Assen", "Molenstraat", 2, "0592GG");
        Assertions.assertFalse(molenstraat1Assen.equals(sameAddress4));
        Address sameAddress5 = new Address("Netherlands", "Assen", "Molenstraat", 1, "059AGG");
        Assertions.assertFalse(molenstraat1Assen.equals(sameAddress5));
    }

    @Test
    void testHashCode() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Address sameAddress = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Assertions.assertEquals(molenstraat1Assen.hashCode(), sameAddress.hashCode());
    }

    @Test
    void testToString() {
        Address molenstraat1Assen = new Address("Netherlands", "Assen", "Molenstraat", 1, "0592GG");
        Assertions.assertEquals(molenstraat1Assen.toString(), "Address{ country='" + "Netherlands"
            + ", city='" + "Assen"
            + ", street='" + "Molenstraat"
            + ", houseNumber=" + "1"
            + ", postalCode='" + "0592GG" + '}');
    }
}