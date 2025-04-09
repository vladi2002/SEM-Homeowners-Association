package nl.tudelft.sem.hoa.unit;

import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaWasCreatedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HoaWasCreatedEventTest {

    @Test
    void testGetHoaId() {
        HoaId beilenWest = new HoaId("krotwijk123");
        HoaWasCreatedEvent beilenIsCreatedEvent = new HoaWasCreatedEvent(beilenWest);

        Assertions.assertEquals(beilenIsCreatedEvent.getHoaId(), beilenWest);
    }
}