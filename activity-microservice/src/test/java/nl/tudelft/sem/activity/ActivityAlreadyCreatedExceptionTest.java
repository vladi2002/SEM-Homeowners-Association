package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.ActivityAlreadyCreatedException;
import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.Organizer;
import org.junit.jupiter.api.Test;

public class ActivityAlreadyCreatedExceptionTest {
    @Test
    public void test() {
        Organizer org = new Organizer("vladi");
        Description desc = new Description("Doing tests");
        ActivityAlreadyCreatedException e = new ActivityAlreadyCreatedException(org, desc);
        assertNotNull(e);
        assertEquals(e.getMessage(), "Organizer: vladi with description: Doing tests already exists.");
    }
}
