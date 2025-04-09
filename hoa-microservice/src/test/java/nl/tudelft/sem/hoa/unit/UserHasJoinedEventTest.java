package nl.tudelft.sem.hoa.unit;

import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.UserHasJoinedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserHasJoinedEventTest {

    @Test
    void getHoaId() {
        HoaId beilenWest = new HoaId("krot123");
        UserHasJoinedEvent bramHasJoined = new UserHasJoinedEvent(beilenWest, "bramy");
        Assertions.assertEquals(bramHasJoined.getHoaId(), beilenWest);
    }

    @Test
    void getUser() {
        HoaId beilenWest = new HoaId("krot123");
        UserHasJoinedEvent bramHasJoined = new UserHasJoinedEvent(beilenWest, "bramy");
        Assertions.assertEquals(bramHasJoined.getUser(), "bramy");
    }
}