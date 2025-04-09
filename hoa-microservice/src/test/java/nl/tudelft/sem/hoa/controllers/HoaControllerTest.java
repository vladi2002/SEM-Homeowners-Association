package nl.tudelft.sem.hoa.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.ExpectedCount.twice;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import nl.tudelft.sem.hoa.authentication.AuthManager;
import nl.tudelft.sem.hoa.authentication.JwtTokenVerifier;
import nl.tudelft.sem.hoa.domain.elections.ElectionRepository;
import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;


@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class HoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Autowired
    HoaRepository hoaRepository;

    @Autowired
    MembersRepository membersRepository;

    @Autowired
    ElectionRepository electionRepository;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);
    }

    @Test
    void findHoa() throws Exception {
        // Arrange
        when(mockAuthenticationManager.getNetId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");

        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoaRepository.save(hoa);

        String bodyRequest = "{\n"
                + "    \"country\": \"SCHLAND\",\n"
                + "    \"city\": \"Test\"\n"
                + "}";
        // Act
        ResultActions result = mockMvc.perform(get("/find")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken")
                .content(bodyRequest));

        // Assert
        result.andExpect(status().isOk());

        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo("Hoa {1, hoaId= Test123, country= SCHLAND, city= Test}\n");
    }

    @Test
    void noSuchHoaTest() throws Exception {
        // Arrange
        when(mockAuthenticationManager.getNetId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");

        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoaRepository.save(hoa);

        String bodyRequest = "{\n"
                + "    \"country\": \"Netherlands\",\n"
                + "    \"city\": \"Assen\"\n"
                + "}";
        // Act
        ResultActions result = mockMvc.perform(get("/find")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken")
                .content(bodyRequest));

        // Assert
        result.andExpect(status().isNotFound());
        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo("");
    }

    @Test
    void leaveHoaSuccess() throws Exception {
        // Arrange
        when(mockAuthenticationManager.getNetId()).thenReturn("bramy");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");

        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoaRepository.save(hoa);

        membersRepository.save(new MemberAppUser("bramy", hoa, new Address("SCHLAND", "Test", "null", 1, "null")));


        // Act
        ResultActions result = mockMvc.perform(post("/leave")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isOk());

        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo("bramy has left the HOA ");
    }

    @Test
    void findHoaFromUsername() throws Exception {
        // Arrange
        when(mockAuthenticationManager.getNetId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");

        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoaRepository.save(hoa);

        membersRepository.save(new MemberAppUser("steen", hoa, new Address("SCHLAND", "Test", "null", 1, "null")));

        // Act
        ResultActions result = mockMvc.perform(get("/find-hoa/" + "steen")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isOk());

        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo("Test123");
    }

    @Test
    void userNotInHoa() throws Exception {
        // Arrange
        when(mockAuthenticationManager.getNetId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");

        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoaRepository.save(hoa);

        membersRepository.save(new MemberAppUser("bramy", hoa, new Address("SCHLAND", "Test", "null", 1, "null")));

        // Act
        ResultActions result = mockMvc.perform(get("/find-hoa/" + "koeny")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isNotFound());
        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo("");
    }


    @Test
    public void joinTest1() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        String response = "{country:Bulgaria,city:Sofia,street:Vasiqa,streetNumber:16,postalCode:1303}";
        mockServer.expect(once(), requestTo("http://localhost:8081/get-address"))
                .andRespond(withSuccess(response, MediaType.TEXT_PLAIN));
        HoaId smg = new HoaId("SMG");
        Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        hoaRepository.save(smgHoa);
        String bodyRequest = "{\n"
                + "    \"hoaId\": \"SMG\"\n"
                + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isOk());
        assertEquals(result.andReturn().getResponse().getContentAsString(), "vladi joined hoa SMG");
    }

    @Test
    public void createTest1() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        String response = "{country:Bulgaria,city:Sofia,street:Vasiqa,streetNumber:16,postalCode:1303}";
        mockServer.expect(twice(), requestTo("http://localhost:8081/get-address"))
                .andRespond(withSuccess(response, MediaType.TEXT_PLAIN));
        HoaId smg = new HoaId("SMG");
        //Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        String bodyRequest = "{\n"
                + "    \"hoaId\": \"SMG\",\n"
                + "    \"country\": \"Bulgaria\",\n"
                + "    \"city\": \"Sofia\"\n"
                + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isOk());
        assertTrue(membersRepository.existsByUsername("vladi"));
        assertTrue(hoaRepository.findById(1).get().getCity().equals("Sofia"));
    }

    @Test
    public void create_correct_country_and_city() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        String response = "{country:Bulgaria,city:Sofia,street:Vasiqa,streetNumber:16,postalCode:1303}";
        mockServer.expect(twice(), requestTo("http://localhost:8081/get-address"))
                .andRespond(withSuccess(response, MediaType.TEXT_PLAIN));
        String bodyRequest = "{\n"
                + "    \"hoaId\": \"SMG\",\n"
                + "    \"country\": \"Bulgaria\",\n"
                + "    \"city\": \"Sofia\"\n"
                + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isOk());
        assertTrue(membersRepository.existsByUsername("vladi"));
        Hoa savedHoa = hoaRepository.findById(1).get();
        assertTrue(savedHoa.getCity().equals("Sofia"));
        assertTrue(savedHoa.getCountry().equals("Bulgaria"));
    }

    @Test
    public void createTest2() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        String response = "{country:Bulgaria,city:Sofia,street:Vasiqa,streetNumber:16,postalCode:1303}";
        mockServer.expect(once(), requestTo("http://localhost:8081/get-address"))
                .andRespond(withException(new IOException()));
        HoaId smg = new HoaId("SMG");
        //Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        String bodyRequest = "{\n"
                + "    \"hoaId\": \"SMG\",\n"
                + "    \"country\": \"Bulgaria\"\n"
                + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createTest3() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        String response = "{country:Bulgaria,city:Sofia,street:Vasiqa,streetNumber:12,postalCode:1303}";
        mockServer.expect(twice(), requestTo("http://localhost:8081/get-address"))
                .andRespond(withSuccess(response, MediaType.TEXT_PLAIN));
        HoaId smg = new HoaId("SMG");
        Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        hoaRepository.save(smgHoa);
        Address address = new Address("Bulgaria", "Sofia", "Vasiqa", 12, "1303");
        MemberAppUser member = new MemberAppUser("vladi", smgHoa, address);
        membersRepository.save(member);
        String bodyRequest = "{\n"
                + "    \"hoaId\": \"Ollie\",\n"
                + "    \"country\": \"Bulgaria\",\n"
                + "    \"city\": \"Sofia\"\n"
                + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isBadRequest());
    }
}