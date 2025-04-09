package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.Organizer;
import org.junit.jupiter.api.Test;

public class OrganizerTest {
    @Test
    public void constructorTest() {
        Organizer org = new Organizer("someone");
        assertNotNull(org);
    }

    @Test
    public void equalsTest() {
        final Organizer org = new Organizer("someone");
        final Organizer org1 = new Organizer("someone");
        final Organizer org2 = new Organizer("somone");
        final Object o1 = new Organizer("someone");
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
        final Organizer o1 = new Organizer("something");
        final Organizer o2 = new Organizer("something");
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    public void toStringTest() {
        Organizer org = new Organizer("someone");
        assertEquals(org.toString(), "someone");
    }

    @Test
    public void toStringTest2() {
        Organizer org = new Organizer("somone");
        assertNotEquals(org.toString(), "someone");
    }
}
