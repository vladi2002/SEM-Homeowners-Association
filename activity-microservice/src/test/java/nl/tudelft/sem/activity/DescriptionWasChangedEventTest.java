package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.DescriptionWasChangedEvent;
import org.junit.jupiter.api.Test;

public class DescriptionWasChangedEventTest {

    @Test
    public void constructorTest() {
        Description d = new Description("Testing");
        DescriptionWasChangedEvent event = new DescriptionWasChangedEvent(d);
        assertNotNull(event);
    }

    @Test
    public void getDescriptionTest() {
        Description d = new Description("Testing");
        DescriptionWasChangedEvent event = new DescriptionWasChangedEvent(d);
        assertEquals(event.getDescription(), d);
    }
}
