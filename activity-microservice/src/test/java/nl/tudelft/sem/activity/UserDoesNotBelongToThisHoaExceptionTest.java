package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.HoaId;
import nl.tudelft.sem.activity.domain.UserDoesNotBelongToThisHoaException;
import org.junit.jupiter.api.Test;

public class UserDoesNotBelongToThisHoaExceptionTest {

    @Test
    public void test() {
        HoaId hoa = new HoaId("Invalid");
        UserDoesNotBelongToThisHoaException e = new UserDoesNotBelongToThisHoaException(hoa);
        assertNotNull(e);
        assertEquals(e.getMessage(), "Not a member of " + hoa.toString() + ". You can't respond to this activity.");
    }
}
