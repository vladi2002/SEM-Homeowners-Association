package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.ActivityNotExistsException;
import org.junit.jupiter.api.Test;

public class ActivityNotExistsExceptionTest {

    @Test
    public void test() {
        ActivityNotExistsException e = new ActivityNotExistsException(3);
        assertNotNull(e);
        assertEquals(e.getMessage(), "Activity: 3 does not exist.");
    }
}
