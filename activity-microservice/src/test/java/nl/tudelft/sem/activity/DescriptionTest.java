package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.Description;
import org.junit.jupiter.api.Test;

public class DescriptionTest {

    @Test
    public void constructorTest() {
        Description desc = new Description("Cooking");
        assertNotNull(desc);
    }

    @Test
    public void equalsTest() {
        final Description org = new Description("someone");
        final Description org1 = new Description("someone");
        final Description org2 = new Description("somone");
        final Object o1 = new Description("someone");
        final Object o2 = new Object();
        final Object o = null;
        assertEquals(org, o1);
        assertEquals(org, org1);
        assertNotEquals(org, o);
        assertNotEquals(org, o2);
        assertNotEquals(org, org2);
    }

    @Test
    public void hashCodeTest() {
        final Description d1 = new Description("something");
        final Description d2 = new Description("something");
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    public void toStringTest() {
        Description desc = new Description("Cooking");
        assertEquals(desc.toString(), "Cooking");
    }

    @Test
    public void toStringTest2() {
        Description desc = new Description("Cooking");
        assertNotEquals(desc.toString(), "cooking");
    }
}
