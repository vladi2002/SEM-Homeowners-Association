package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.activity.domain.ActivitiesNotFoundException;
import nl.tudelft.sem.activity.domain.Activity;
import nl.tudelft.sem.activity.domain.ActivityAlreadyCreatedException;
import nl.tudelft.sem.activity.domain.ActivityNotExistsException;
import nl.tudelft.sem.activity.domain.ActivityRepository;
import nl.tudelft.sem.activity.domain.ActivityResponse;
import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.HoaId;
import nl.tudelft.sem.activity.domain.Organizer;
import nl.tudelft.sem.activity.domain.RegistrationService;
import nl.tudelft.sem.activity.domain.Response;
import nl.tudelft.sem.activity.domain.ResponseOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RegistrationServiceTest {

    private ActivityRepository repo = Mockito.mock(ActivityRepository.class);

    private final transient HoaId hoa1 = new HoaId("Pulse");
    private final transient HoaId hoa2 = new HoaId("Building 28");
    private final transient HoaId hoa3 = new HoaId("Illegal");
    private final transient Date dateFuture1 = new Date(2023, 12, 20, 20, 30);
    private final transient Date dateFuture2 = new Date(2024, 2, 29, 16, 45);
    private final transient Date datePast1 = new Date(2019, 7, 28, 10, 15);
    private final transient Date datePast2 = new Date(2016, 1, 13, 8, 30);

    private final transient Organizer vladi = new Organizer("vladi");
    private final transient Organizer alex = new Organizer("alex");
    private final transient Organizer bram = new Organizer("bram");
    private final transient Organizer rafa = new Organizer("rafa");

    private final transient Description d1 = new Description("Club 33");
    private final transient Description d2 = new Description("Rest");
    private final transient Description d3 = new Description("SEM sign-off");
    private final transient Description d4 = new Description("SEM check");
    private final transient Description d5 = new Description("Piano concert");
    private final transient Activity a1 = new Activity(vladi, d1, dateFuture1, hoa1);
    private final transient Activity a2 = new Activity(bram, d2, dateFuture2, hoa1);
    private final transient Activity a3 = new Activity(alex, d3, datePast1, hoa2);
    private final transient Activity a4 = new Activity(rafa, d4, datePast2, hoa2);
    private final transient Activity a5 = new Activity(alex, d5, dateFuture1, hoa2);

    /**
     * Mocks the database.
     */
    @BeforeEach
    public void setup() {
        when(repo.existsByOrganizerAndDescription(rafa, d4)).thenReturn(false);
        when(repo.existsByOrganizerAndDescription(vladi, d1)).thenReturn(true);
        when(repo.existsById(1)).thenReturn(false);
        when(repo.existsById(2)).thenReturn(true);
        when(repo.existsById(3)).thenReturn(true);
        when(repo.existsById(4)).thenReturn(true);
        when(repo.existsById(5)).thenReturn(true);
        when(repo.existsById(6)).thenReturn(true);
        when(repo.findByHoaId(hoa1)).thenReturn(Optional.of(List.of(a1, a2)));
        when(repo.findByHoaId(hoa2)).thenReturn(Optional.of(List.of(a3, a4, a5)));
        when(repo.findByHoaId(hoa3)).thenThrow(RuntimeException.class);
        when(repo.findById(2)).thenReturn(Optional.of(a2));
        when(repo.findById(3)).thenReturn(Optional.of(a3));
        when(repo.findById(4)).thenReturn(Optional.of(a4));
        when(repo.findById(5)).thenReturn(Optional.of(a5));
        when(repo.findById(6)).thenThrow(RuntimeException.class);
    }

    @Test
    public void constructorTest() {
        RegistrationService reg = new RegistrationService(repo);
        assertNotNull(reg);
    }

    @Test
    public void checkActivityIsUniqueTest() {
        RegistrationService reg = new RegistrationService(repo);
        assertTrue(reg.checkActivityIsUnique(rafa, d4));
        verify(repo).existsByOrganizerAndDescription(rafa, d4);
    }

    @Test
    public void checkActivityExistsByIdTest() {
        RegistrationService reg = new RegistrationService(repo);
        assertFalse(reg.checkActivityExistsById(1));
        verify(repo).existsById(1);
    }

    @Test
    public void findAllActivitiesTest() throws Exception {
        RegistrationService reg = new RegistrationService(repo);
        List<Activity> responses = reg.findAllActivities(hoa1).get();
        assertEquals(responses, List.of(a1, a2));
        verify(repo).findByHoaId(hoa1);
    }

    @Test
    public void findAllActivitiesExceptionTest() throws Exception {
        RegistrationService reg = new RegistrationService(repo);
        Exception e = assertThrows(ActivitiesNotFoundException.class, () -> reg.findAllActivities(hoa3));
        assertEquals(e.getMessage(), "HOA: " + hoa3.toString() + " does not correspond to any activity");
        verify(repo).findByHoaId(hoa3);
    }

    @Test
    public void findActivityByIdTest1() throws Exception {
        RegistrationService reg = new RegistrationService(repo);
        assertEquals(a2, reg.findActivityById(2).get());
        assertEquals(a3, reg.findActivityById(3).get());
        assertEquals(a4, reg.findActivityById(4).get());
        assertEquals(a5, reg.findActivityById(5).get());
        verify(repo).findById(2);
        verify(repo).findById(3);
        verify(repo).findById(4);
        verify(repo).findById(5);
    }

    @Test
    public void findActivityByIdTest2() {
        RegistrationService reg = new RegistrationService(repo);
        Exception e = assertThrows(ActivityNotExistsException.class, () -> reg.findActivityById(1));
        assertEquals(e.getMessage(), "Activity: 1 does not exist.");
    }

    @Test
    public void findActivityByIdTest3() {
        RegistrationService reg = new RegistrationService(repo);
        Exception e = assertThrows(ActivityNotExistsException.class, () -> reg.findActivityById(6));
        assertEquals(e.getMessage(), "Activity: 6 does not exist.");
    }

    @Test
    public void registerResponseTest() {
        RegistrationService reg = new RegistrationService(repo);
        Response res = new ActivityResponse(ResponseOption.INTERESTED, "Bram");
        reg.registerResponse(res, a3);
        verify(repo).save(a3);
    }

    @Test
    public void registerActivityTestSuccess() throws Exception {
        RegistrationService reg = new RegistrationService(repo);
        Activity a = new Activity(rafa, d4, datePast2, hoa3);
        assertEquals(a, reg.registerActivity(rafa, d4, datePast2, hoa3));
        verify(repo).save(a);
    }

    @Test
    public void registerActivityTestFail() {
        RegistrationService reg = new RegistrationService(repo);
        Exception e = assertThrows(ActivityAlreadyCreatedException.class,
            () -> reg.registerActivity(vladi, d1, datePast2, hoa3));
        assertEquals(e.getMessage(), "Organizer: " + vladi.toString() + " with description: "
            + d1.toString() + " already exists.");
    }
}
