package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import nl.tudelft.sem.activity.domain.Activity;
import nl.tudelft.sem.activity.domain.Builder;
import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.HoaId;
import nl.tudelft.sem.activity.domain.Organizer;
import nl.tudelft.sem.activity.domain.Response;
import nl.tudelft.sem.activity.domain.ResponseBuilder;
import nl.tudelft.sem.activity.domain.ResponseOption;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ActivityTest {

    @Test
    public void constructorTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        assertNotNull(a1);
    }

    //    @Test
    //    public void getOrganizerTest() {
    //        Organizer vladi = new Organizer("vladi");
    //        Description d1 = new Description("Club 33");
    //        HoaId hoa1 = new HoaId("Pulse");
    //        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
    //        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
    //        assertEquals(a1.getOrganizer(), vladi);
    //    }

    //    @Test
    //    public void getDescriptionTest() {
    //        Organizer vladi = new Organizer("vladi");
    //        Description d1 = new Description("Club 33");
    //        HoaId hoa1 = new HoaId("Pulse");
    //        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
    //        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
    //        assertEquals(a1.getDescription(), d1);
    //    }

    @Test
    public void getDateTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        assertEquals(a1.getDate(), dateFuture1);
    }

    @Test
    public void getHoaIdTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        assertEquals(a1.getHoaId(), hoa1);
    }

    @Test
    public void getIdTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        assertEquals(a1.getId(), 0);
    }

    @Test
    public void setIdTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        a1.setId(2);
        assertEquals(a1.getId(), 2);
    }

    @Test
    public void getEmptyResponsesTest() {
        final Organizer vladi = new Organizer("vladi");
        final Description d1 = new Description("Club 33");
        final HoaId hoa1 = new HoaId("Pulse");
        final Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Builder builder = new ResponseBuilder();
        builder.setResponderName(vladi.toString());
        builder.setResponseOption(ResponseOption.GOING);
        Response responseActivity = builder.build();
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        List<Response> responses = new ArrayList<>();
        assertEquals(a1.getResponseList(), responses);
    }

    @Test
    public void getAssignedOrganizerResponseTest() {
        final Organizer vladi = new Organizer("vladi");
        final Description d1 = new Description("Club 33");
        final HoaId hoa1 = new HoaId("Pulse");
        final Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Builder builder = new ResponseBuilder();
        builder.setResponderName(vladi.toString());
        builder.setResponseOption(ResponseOption.GOING);
        Response responseActivity = builder.build();
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        a1.setOrganizerResponseToGoing();
        List<Response> responses = new ArrayList<>();
        responses.add(responseActivity);
        assertEquals(a1.getResponseList(), responses);
    }

    @Test
    public void recordResponsesTest() {
        List<Response> responses = new ArrayList<>();
        final Organizer vladi = new Organizer("vladi");
        final Description d1 = new Description("Club 33");
        final HoaId hoa1 = new HoaId("Pulse");
        final Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Builder builder = new ResponseBuilder();
        builder.setResponderName(vladi.toString());
        builder.setResponseOption(ResponseOption.GOING);
        Response responseActivity = builder.build();
        responses.add(responseActivity);
        builder.setResponderName("rafa");
        builder.setResponseOption(ResponseOption.NOT_INTERESTED);
        Response responseActivity2 = builder.build();
        responses.add(responseActivity2);
        builder.setResponderName("alex");
        builder.setResponseOption(ResponseOption.INTERESTED);
        Response responseActivity3 = builder.build();
        responses.add(responseActivity3);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        a1.setOrganizerResponseToGoing();
        a1.addResponse(responseActivity2);
        a1.addResponse(responseActivity3);
        assertEquals(a1.getResponseList(), responses);
    }

    //    @Test
    //    public void changeDescriptionTest() {
    //        Organizer vladi = new Organizer("vladi");
    //        Description d1 = new Description("Club 33");
    //        HoaId hoa1 = new HoaId("Pulse");
    //        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
    //        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
    //        a1.setOrganizerResponseToGoing();
    //        Description d2 = new Description("Rest");
    //        a1.changeDescription(d2);
    //        assertEquals(a1.getDescription(), d2);
    //    }

    //    @Test
    //    public void setResponseListTest() {
    //        List<Response> responses = new ArrayList<>();
    //        final Organizer vladi = new Organizer("vladi");
    //        final Description d1 = new Description("Club 33");
    //        final HoaId hoa1 = new HoaId("Pulse");
    //        final Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
    //        Builder builder = new ResponseBuilder();
    //
    //        builder.setResponderName(vladi.toString());
    //        builder.setResponseOption(ResponseOption.GOING);
    //        Response responseActivity = builder.build();
    //        responses.add(responseActivity);
    //        builder.setResponderName("rafa");
    //        builder.setResponseOption(ResponseOption.NOT_INTERESTED);
    //        Response responseActivity2 = builder.build();
    //        builder.setResponderName("alex");
    //        builder.setResponseOption(ResponseOption.INTERESTED);
    //        Response responseActivity3 = builder.build();
    //        responses.add(responseActivity2);
    //        responses.add(responseActivity3);
    //        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
    //        a1.setOrganizerResponseToGoing();
    //        a1.setResponseList(responses);
    //        assertEquals(a1.getResponseList(), responses);
    //    }

    @Test
    public void equalsTest1() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        a1.setOrganizerResponseToGoing();
        Organizer vladi2 = new Organizer("vladi");
        Description d2 = new Description("Club 33");
        HoaId hoa2 = new HoaId("Pulse");
        Date dateFuture2 = new Date(2023, 12, 20, 20, 30);
        Activity a2 = new Activity(vladi2, d2, dateFuture2, hoa2);
        a2.setOrganizerResponseToGoing();
        Activity a3 = a1;
        assertEquals(a1, a2);
        assertEquals(a1, a3);
    }

    @Test
    public void equalsTest2() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        a1.setOrganizerResponseToGoing();
        Object a2 = new Object();
        Object a3 = null;
        Activity a4 = null;
        assertNotEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertNotEquals(a1, a4);
        Activity a5 = new Activity(vladi, d1, dateFuture1, hoa1);
        a5.setOrganizerResponseToGoing();
        a5.setId(2);
        assertNotEquals(a1, a5);
    }

    @Test
    public void hashCodeTest() {
        Organizer vladi = new Organizer("vladi");
        Description d1 = new Description("Club 33");
        HoaId hoa1 = new HoaId("Pulse");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        a1.setOrganizerResponseToGoing();
        Organizer vladi2 = new Organizer("vladi");
        Description d2 = new Description("Club 33");
        HoaId hoa2 = new HoaId("Pulse");
        Date dateFuture2 = new Date(2023, 12, 20, 20, 30);
        Activity a2 = new Activity(vladi2, d2, dateFuture2, hoa2);
        a2.setOrganizerResponseToGoing();
        Activity a3 = a1;
        assertEquals(a1.hashCode(), a2.hashCode());
        assertEquals(a1.hashCode(), a3.hashCode());
    }

    @Test
    public void toStringTest() {
        Organizer vladi2 = new Organizer("vladi");
        Description d2 = new Description("Club 33");
        HoaId hoa2 = new HoaId("Pulse");
        Date dateFuture2 = new Date(2023, 12, 20, 20, 30);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMAN);
        formatter.format(dateFuture2);
        Activity a2 = new Activity(vladi2, d2, dateFuture2, hoa2);
        a2.setOrganizerResponseToGoing();
        String expected = "Activity: 0, organized by: vladi, on " + dateFuture2.toString()
            + " at Pulse with description: Club 33. Responses: [GOING vladi]";
        assertEquals(expected, a2.toString());
    }
}
