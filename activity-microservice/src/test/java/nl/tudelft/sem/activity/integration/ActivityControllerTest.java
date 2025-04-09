package nl.tudelft.sem.activity.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Locale;
import nl.tudelft.sem.activity.authentication.AuthManager;
import nl.tudelft.sem.activity.authentication.JwtTokenVerifier;
import nl.tudelft.sem.activity.domain.Activity;
import nl.tudelft.sem.activity.domain.ActivityNotExistsException;
import nl.tudelft.sem.activity.domain.ActivityRepository;
import nl.tudelft.sem.activity.domain.ActivityResponse;
import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.HoaId;
import nl.tudelft.sem.activity.domain.Organizer;
import nl.tudelft.sem.activity.domain.Response;
import nl.tudelft.sem.activity.domain.ResponseOption;
import nl.tudelft.sem.activity.domain.UserDoesNotBelongToThisHoaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Autowired
    ActivityRepository repo;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private final transient String hoaResourceUrl = "http://localhost:8083/find-hoa/";
    private final transient String authorizationLiteral = "Authorization";

    private final transient HoaId hoa1 = new HoaId("Pulse");
    private final transient HoaId hoa2 = new HoaId("Building 28");
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMAN);
    private final transient Date dateFuture1 = formatter.parse("2028-12-20 20:30");
    private final transient Date dateFuture2 = formatter.parse("2027-02-29 16:45");
    private final transient Date datePast1 = formatter.parse("2019-07-28 10:15");
    private final transient Date datePast2 = formatter.parse("2016-01-13 08:30");

    private final transient Organizer vladi = new Organizer("vladi");
    private final transient String vladiToken = "vladi";
    private final transient Organizer alex = new Organizer("alex");
    private final transient String alexToken = "alex";
    private final transient Organizer bram = new Organizer("bram");
    private final transient String bramToken = "bram";
    private final transient Organizer rafa = new Organizer("rafa");
    private final transient String rafaToken = "rafa";

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

    public ActivityControllerTest() throws ParseException {
    }

    /**
     * Sets up the mocked connections.
     *
     * @throws Exception if something goes wrong
     */
    @BeforeEach
    public void setup() throws Exception {
        when(mockAuthenticationManager.getUsername()).thenReturn(vladi.toString());
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        //when(mockJwtTokenVerifier.validateToken(vladiToken)).thenReturn(true);
        when(mockJwtTokenVerifier.getUsernameFromToken(anyString())).thenReturn(vladi.toString());
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);
        a1.setOrganizerResponseToGoing();
        a2.setOrganizerResponseToGoing();
        a3.setOrganizerResponseToGoing();
        a4.setOrganizerResponseToGoing();
        a5.setOrganizerResponseToGoing();
    }

    @Test
    public void findByIdTest1() throws Exception {
        repo.save(a1);
        ResultActions result = mockMvc.perform(get("/byId/1")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Bearer MockedToken"));
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        assertEquals(response, "Activity: " + a1.toString());
    }

    @Test
    public void findByIdTest2() throws Exception {
        mockMvc.perform(get("/byId/1")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer MockedToken")).andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
            .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST.toString() + " \""
                + new ActivityNotExistsException(1).getMessage() + "\"", result.getResolvedException().getMessage()));
    }

    @Test
    public void findAllActivitiesHoaTest1() throws Exception {
        mockServer.expect(once(), requestTo(hoaResourceUrl + vladi.toString()))
            .andRespond(withSuccess(hoa1.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        ResultActions result = mockMvc.perform(get("/getAll")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer MockedToken"));
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        assertEquals(response, "Activities: " + List.of(a1, a2).toString());
    }

    @Test
    public void findAllActivitiesHoaTest2() throws Exception {
        HoaId hoa3 = new HoaId("Missing");
        mockServer.expect(once(), requestTo(hoaResourceUrl + vladi.toString()))
            .andRespond(withSuccess(hoa3.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        mockMvc.perform(get("/getAll")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken")).andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
            .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST.toString() + " \""
                + "No value present" + "\"", result.getResolvedException().getMessage()));
    }

    @Test
    public void findAllUpcomingActivitiesHoaTest1() throws Exception {
        when(mockAuthenticationManager.getUsername()).thenReturn(alex.toString());
        when(mockJwtTokenVerifier.getUsernameFromToken(anyString())).thenReturn(alex.toString());
        mockServer.expect(once(), requestTo(hoaResourceUrl + alex.toString()))
            .andRespond(withSuccess(hoa2.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        ResultActions result = mockMvc.perform(get("/getAllUpcoming")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer MockedToken"));
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        assertEquals(response, "Activities: " + List.of(a5).toString());
    }

    @Test
    public void findAllUpcomingActivitiesHoaTest2() throws Exception {
        HoaId hoa3 = new HoaId("Missing");
        when(mockAuthenticationManager.getUsername()).thenReturn(alex.toString());
        when(mockJwtTokenVerifier.getUsernameFromToken(anyString())).thenReturn(alex.toString());
        mockServer.expect(once(), requestTo(hoaResourceUrl + alex.toString()))
            .andRespond(withSuccess(hoa3.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        mockMvc.perform(get("/getAllUpcoming").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken")).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST.toString() + " \""
                    + "No value present" + "\"", result.getResolvedException().getMessage()));
    }

    @Test
    public void registerActivityTest1() throws Exception {
        mockServer.expect(once(), requestTo(hoaResourceUrl + vladi.toString()))
                .andRespond(withSuccess(hoa1.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        String bodyRequest = "{\n"
                + "    \"description\": \"No ideas today\",\n"
                + "    \"date\": \"2022-20-12 12:30\"\n"
                + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/registerActivity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest)
                .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isOk());
        String response = result.andReturn().getResponse().getContentAsString();
        assertEquals(response, "Activity created successfully! " + repo.findById(6).get().toString());
    }

    @Test
    public void registerActivityTest2() throws Exception {
        mockServer.expect(once(), requestTo(hoaResourceUrl + vladi.toString()))
                .andRespond(withSuccess(hoa1.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        String bodyRequest = "{\n"
                + "    \"description\": \"No ideas today\",\n"
                + "    \"date\": \"hxjfh30\"\n"
                + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/registerActivity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST.toString() + " \""
                        + HttpStatus.BAD_REQUEST.toString()
                        + " \"Date not recognized." + "\"\"", result.getResolvedException().getMessage()));
    }

    @Test
    public void registerActivityTest3() throws Exception {
        mockServer.expect(once(), requestTo(hoaResourceUrl + vladi.toString()))
                .andRespond(withException(new InvalidPropertiesFormatException("HOA this user belongs to was not found")));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        String bodyRequest = "{\n"
                + "    \"description\": \"No ideas today\",\n"
                + "    \"date\": \"hxjfh30\"\n"
                + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/registerActivity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST.toString()
                        + " \"I/O error on GET request for \"http://localhost:8083/find-hoa/vladi\": "
                        + "HOA this user belongs to was not found; "
                        + "nested exception is java.util.InvalidPropertiesFormatException: "
                        + "HOA this user belongs to was not found\"", result.getResolvedException().getMessage()));
    }

    @Test
    public void respondActivityTest1() throws Exception {
        mockServer.expect(once(), requestTo(hoaResourceUrl + vladi.toString()))
                    .andRespond(withSuccess(hoa1.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        String bodyRequest = "{\n"
                    + "    \"activityId\": \"2\",\n"
                    + "    \"response\": \"NOT_INTERESTED\"\n"
                    + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/respond")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bodyRequest)
                            .header("Authorization", "Bearer MockedToken"))
                    .andExpect(status().isOk());
        Response created = new ActivityResponse(ResponseOption.NOT_INTERESTED, vladi.toString());
        String response = result.andReturn().getResponse().getContentAsString();
        assertEquals(response, created.toString() + " added to " + repo.findById(2).get().toString());
    }

    @Test
    public void respondActivityTest2() throws Exception {
        mockServer.expect(once(), requestTo(hoaResourceUrl + vladi.toString()))
                .andRespond(withSuccess(hoa1.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        String bodyRequest = "{\n"
                + "    \"activityId\": \"3\",\n"
                + "    \"response\": \"NOT_INTERESTED\"\n"
                + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/respond")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST.toString() + " \""
                                + new UserDoesNotBelongToThisHoaException(hoa1).getMessage() + "\"",
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void respondActivityTest3() throws Exception {
        mockServer.expect(once(), requestTo(hoaResourceUrl + vladi.toString()))
                .andRespond(withSuccess(hoa1.toString(), MediaType.TEXT_PLAIN));
        repo.save(a1);
        repo.save(a2);
        repo.save(a3);
        repo.save(a4);
        repo.save(a5);
        String bodyRequest = "{\n"
                + "    \"activityId\": \"6\",\n"
                + "    \"response\": \"NOT_INTERESTED\"\n"
                + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/respond")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST.toString() + " \""
                                + new ActivityNotExistsException(6).getMessage() + "\"",
                        result.getResolvedException().getMessage()));
    }
}
