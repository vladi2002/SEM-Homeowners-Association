package nl.tudelft.sem.activity;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.util.Date;
import nl.tudelft.sem.activity.domain.Activity;
import nl.tudelft.sem.activity.domain.ActivityRepository;
import nl.tudelft.sem.activity.domain.Builder;
import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.HoaId;
import nl.tudelft.sem.activity.domain.Organizer;
import nl.tudelft.sem.activity.domain.Response;
import nl.tudelft.sem.activity.domain.ResponseBuilder;
import nl.tudelft.sem.activity.domain.ResponseOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository repo;
    private Activity a1;
    private Activity a2;

    /**
     * sets up the repository with given activities before each use of the database.
     */
    @BeforeEach
    public void setup() {
        Organizer vladi = new Organizer("vladi");
        Organizer rafa = new Organizer("rafa");
        Description d1 = new Description("Club 33");
        Description d2 = new Description("Cooking");
        HoaId hoa1 = new HoaId("Pulse");
        HoaId hoa2 = new HoaId("Building 28");
        Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
        Date dateFuture2 = new Date(2024, 4, 6, 12, 00);
        a1 = new Activity(vladi, d1, dateFuture1, hoa1);
        a2 = new Activity(rafa, d2, dateFuture2, hoa2);
        Builder builder = new ResponseBuilder();
        builder.setResponderName("rafa");
        builder.setResponseOption(ResponseOption.NOT_INTERESTED);
        final Response responseActivity2 = builder.build();
        builder.setResponderName("alex");
        builder.setResponseOption(ResponseOption.INTERESTED);
        final Response responseActivity3 = builder.build();
        builder.setResponderName("bram");
        builder.setResponseOption(ResponseOption.GOING);
        final Response responseActivity4 = builder.build();
        builder.setResponderName("roland");
        builder.setResponseOption(ResponseOption.NOT_INTERESTED);
        final Response responseActivity5 = builder.build();
        builder.setResponderName("jelt");
        builder.setResponseOption(ResponseOption.INTERESTED);
        final Response responseActivity6 = builder.build();
        a1.addResponse(responseActivity2);
        a1.addResponse(responseActivity3);
        a2.addResponse(responseActivity4);
        a2.addResponse(responseActivity5);
        a2.addResponse(responseActivity6);
        repo.save(a1);
        repo.save(a2);
    }

    /**
     * Tests the existsById method.
     */
    @Test
    public void existsByIdTest() {
        assertTrue("Activity 1 exists", repo.existsById(1));
        assertTrue("Activity 2 exists", repo.existsById(2));
        assertFalse("Activity 0 does not exist", repo.existsById(0));
        assertFalse("Activity 3 does not exist", repo.existsById(3));
    }

    /**
     * Tests the existsByOrganizerAndDescription method.
     */
    @Test
    public void existsByOrganizerAndDescriptionTest() {
        assertTrue("Activity 1 exists", repo.existsByOrganizerAndDescription(new Organizer("vladi"),
            new Description("Club 33")));
        assertTrue("Activity 2 exists", repo.existsByOrganizerAndDescription(new Organizer("rafa"),
            new Description("Cooking")));
        assertFalse("This one doesn't exist", repo.existsByOrganizerAndDescription(new Organizer("rafa"),
            new Description("Club 33")));
    }

    /**
     * Tests the findById method.
     */
    @Test
    public void findByIdTest() {
        Activity a = repo.findById(1).get();
        Activity b = repo.findById(2).get();
        assertTrue("The 2 activities must be equal", a.equals(a1));
        assertTrue("The 2 activities must be equal", b.equals(a2));
    }

    /**
     * Tests the findByHoaId method.
     */
    @Test
    public void findByHoaIdTest() {
        Activity a = repo.findByHoaId(new HoaId("Pulse")).get().get(0);
        Activity b = repo.findByHoaId(new HoaId("Building 28")).get().get(0);
        assertTrue("The 2 activities must be equal", a.equals(a1));
        assertTrue("The 2 activities must be equal", b.equals(a2));
    }
}
