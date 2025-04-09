package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.ActivityResponse;
import nl.tudelft.sem.activity.domain.ResponseOption;
import org.junit.jupiter.api.Test;

public class ActivityResponseTest {

    @Test
    public void constructorTest() {
        ActivityResponse res = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        assertNotNull(res);
    }

    @Test
    public void getResponseTest() {
        ActivityResponse res = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        assertEquals(res.getResponse(), ResponseOption.INTERESTED);
    }

    @Test
    public void setResponseTest() {
        ActivityResponse res = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        res.setResponse(ResponseOption.NOT_INTERESTED);
        assertEquals(res.getResponse(), ResponseOption.NOT_INTERESTED);
    }

    @Test
    public void getResponderNameTest() {
        ActivityResponse res = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        assertEquals(res.getResponderName(), "Bram");
    }

    @Test
    public void toStringTest() {
        ActivityResponse res = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        String expected = "INTERESTED Bram";
        assertEquals(res.toString(), expected);
    }

    @Test
    public void toStringTest2() {
        ActivityResponse res = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        String expected = "INTERESTED Bam";
        assertNotEquals(res.toString(), expected);
    }

    @Test
    public void equalsTest() {
        ActivityResponse res1 = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        ActivityResponse res2 = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        assertEquals(res1, res2);
    }

    @Test
    public void equalsTest2() {
        ActivityResponse res1 = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        ActivityResponse res2 = new ActivityResponse(ResponseOption.INTERESTED, "Bam");
        ActivityResponse res3 = new ActivityResponse(ResponseOption.GOING, "Bram");
        assertNotEquals(res1, res2);
        assertNotEquals(res1, res3);
    }

    @Test
    public void equalsTest3() {
        ActivityResponse res1 = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        Object res2 = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        assertEquals(res1, res2);
    }

    @Test
    public void equalsTest4() {
        ActivityResponse res1 = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        Object res2 = new Object();
        Object res3 = null;
        ActivityResponse res4 = null;
        assertNotEquals(res1, res2);
        assertNotEquals(res1, res3);
        assertNotEquals(res1, res4);
    }

    @Test
    public void hashCodeTest() {
        ActivityResponse res1 = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        ActivityResponse res2 = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        assertEquals(res1.hashCode(), res2.hashCode());
    }
}
