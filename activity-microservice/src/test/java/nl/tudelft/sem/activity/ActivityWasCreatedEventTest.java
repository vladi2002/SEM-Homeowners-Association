package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import nl.tudelft.sem.activity.domain.ActivityWasCreatedEvent;
import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.HoaId;
import nl.tudelft.sem.activity.domain.Organizer;
import org.junit.jupiter.api.Test;


public class ActivityWasCreatedEventTest {

    @Test
    public void constructorTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        ActivityWasCreatedEvent event = new ActivityWasCreatedEvent(1, vladi, d1, dateFuture1, hoa1);
        assertNotNull(event);
    }

    @Test
    public void getEventDescriptionTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        ActivityWasCreatedEvent event = new ActivityWasCreatedEvent(1, vladi, d1, dateFuture1, hoa1);
        assertEquals(event.getEventDescription(), d1);
    }

    @Test
    public void setEventDescriptionTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        ActivityWasCreatedEvent event = new ActivityWasCreatedEvent(1, vladi, d1, dateFuture1, hoa1);
        Description d2 = new Description("Club 33");
        event.setEventDescription(d2);
        assertEquals(event.getEventDescription(), d2);
    }

    @Test
    public void getIdTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        ActivityWasCreatedEvent event = new ActivityWasCreatedEvent(1, vladi, d1, dateFuture1, hoa1);
        assertEquals(event.getId(), 1);
    }

    @Test
    public void getOrganizerTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        ActivityWasCreatedEvent event = new ActivityWasCreatedEvent(1, vladi, d1, dateFuture1, hoa1);
        assertEquals(event.getOrganizer(), vladi);
    }

    @Test
    public void getDateTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        ActivityWasCreatedEvent event = new ActivityWasCreatedEvent(1, vladi, d1, dateFuture1, hoa1);
        assertEquals(event.getDate(), dateFuture1);
    }

    @Test
    public void setDateTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        ActivityWasCreatedEvent event = new ActivityWasCreatedEvent(1, vladi, d1, dateFuture1, hoa1);
        Date datePast1 = new Date(2019, 12, 20, 20, 30);
        event.setDate(datePast1);
        assertEquals(event.getDate(), datePast1);
    }

    @Test
    public void getHoaIdTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        ActivityWasCreatedEvent event = new ActivityWasCreatedEvent(1, vladi, d1, dateFuture1, hoa1);
        assertEquals(event.getHoaId(), hoa1);
    }
}
