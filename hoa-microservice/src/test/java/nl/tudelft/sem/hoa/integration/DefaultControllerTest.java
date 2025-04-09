package nl.tudelft.sem.hoa.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import nl.tudelft.sem.hoa.authentication.AuthManager;
import nl.tudelft.sem.hoa.authentication.JwtTokenVerifier;
import nl.tudelft.sem.hoa.domain.elections.Election;
import nl.tudelft.sem.hoa.domain.elections.ElectionRepository;
import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import nl.tudelft.sem.hoa.domain.vote.ElectionVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import nl.tudelft.sem.hoa.domain.vote.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DefaultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Autowired
    HoaRepository rep;

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
    public void applyBoardTest1() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        MemberAppUser member = new MemberAppUser("vladi", new Hoa(new HoaId("SMG"),
                "Bulgaria", "Sofia"), new Address("Bulgaria", "Sofia", "Street", 12, "1303"));
        membersRepository.save(member);
        Thread.sleep(11000);
        mockMvc.perform(post("/apply-board")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("The user successfully applied to the board",
                        result.getResponse().getContentAsString()));
        member.setRole("APPLICANT");
        MemberAppUser updated = membersRepository.findMemberAppUsersByUsername("vladi").get();
        assertEquals(member.toString(), updated.toString());
    }

    @Test
    public void applyBoardTest2() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        MemberAppUser member = new MemberAppUser("vladi", new Hoa(new HoaId("SMG"),
                "Bulgaria", "Sofia"), new Address("Bulgaria", "Sofia", "Street", 12, "1303"));
        membersRepository.save(member);
        mockMvc.perform(post("/apply-board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void applyBoardTest3() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        MemberAppUser member = new MemberAppUser("vladi", new Hoa(new HoaId("SMG"),
                "Bulgaria", "Sofia"), new Address("Bulgaria", "Sofia", "Street", 12, "1303"));
        mockMvc.perform(post("/apply-board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void applyBoardTest4() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        MemberAppUser member = new MemberAppUser("vladi", new Hoa(new HoaId("SMG"),
                "Bulgaria", "Sofia"), new Address("Bulgaria", "Sofia", "Street", 12, "1303"));
        member.setBoardMember(true);
        membersRepository.save(member);

        mockMvc.perform(post("/apply-board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("The user successfully applied to the board",
                        result.getResponse().getContentAsString()));
        member.setRole("APPLICANT");
        MemberAppUser updated = membersRepository.findMemberAppUsersByUsername("vladi").get();
        assertEquals(member.toString(), updated.toString());
    }

    @Test
    public void applyBoardTest5() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        MemberAppUser member = new MemberAppUser("vladi", new Hoa(new HoaId("SMG"),
                "Bulgaria", "Sofia"), new Address("Bulgaria", "Sofia", "Street", 12, "1303"));
        member.setBoardMember(true);
        membersRepository.save(member);
        Thread.sleep(16000);
        mockMvc.perform(post("/apply-board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findBoardMembers1() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        HoaId smg = new HoaId("SMG");
        Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        Address address = new Address("Bulgaria", "Sofia", "Street", 12, "1303");
        MemberAppUser member = new MemberAppUser("vladi", smgHoa, address);
        member.setBoardMember(true);
        membersRepository.save(member);
        MemberAppUser member2 = new MemberAppUser("rafa", smgHoa, address);
        member2.setBoardMember(true);
        membersRepository.save(member2);
        MemberAppUser member3 = new MemberAppUser("alex", smgHoa, address);
        member3.setBoardMember(true);
        membersRepository.save(member3);
        MemberAppUser member4 = new MemberAppUser("roland", smgHoa, address);
        membersRepository.save(member4);
        MemberAppUser member5 = new MemberAppUser("jelt", smgHoa, address);
        membersRepository.save(member5);
        Election election = new Election("1", smg.toString());
        election.updateVote(new ElectionVote(new TypelessVote("vladi"), "1", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("alex"), "1", "roland"));
        election.updateVote(new ElectionVote(new TypelessVote("rafa"), "1", "vladi"));
        election.updateVote(new ElectionVote(new TypelessVote("jelt"), "1", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("roland"), "1", "vladi"));
        electionRepository.save(election);
        List<MemberAppUser> board = electionRepository.findByBoard(smg);
        ResultActions result = mockMvc.perform(get("/find-board/SMG")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));
        result.andExpect(status().isOk());
        String output = "";
        output += member.toString() + "\n" + member2.toString() + "\n" + member3.toString() + "\n";
        assertEquals(result.andReturn().getResponse().getContentAsString(), output);
    }

    @Test
    public void electionVoteTest() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        Vote newVote = new TypelessVote("vladi");
        ElectionVote vote = new ElectionVote(newVote, "battleROyal", "alex");

        HoaId smg = new HoaId("SMG");
        Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        Address address = new Address("Bulgaria", "Sofia", "Street", 12, "1303");
        MemberAppUser member = new MemberAppUser("vladi", smgHoa, address);
        member.setRole("APPLICANT");
        membersRepository.save(member);
        MemberAppUser member2 = new MemberAppUser("rafa", smgHoa, address);
        membersRepository.save(member2);
        MemberAppUser member3 = new MemberAppUser("alex", smgHoa, address);
        member3.setRole("APPLICANT");
        membersRepository.save(member3);
        MemberAppUser member4 = new MemberAppUser("roland", smgHoa, address);
        member4.setRole("APPLICANT");
        membersRepository.save(member4);
        MemberAppUser member5 = new MemberAppUser("jelt", smgHoa, address);
        rep.save(smgHoa);
        membersRepository.save(member5);
        Election election = new Election("battleROyal", smg.toString());
        //election.updateVote(new ElectionVote(new TypelessVote("vladi"), "1", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("alex"), "battleROyal", "roland"));
        election.updateVote(new ElectionVote(new TypelessVote("rafa"), "battleROyal", "vladi"));
        election.updateVote(new ElectionVote(new TypelessVote("jelt"), "battleROyal", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("roland"), "battleROyal", "vladi"));
        electionRepository.save(election);

        String bodyRequest = "{\n"
                + "    \"electionId\": \"battleROyal\",\n"
                + "    \"hoaId\": \"SMG\",\n"
                + "    \"applicantId\": \"alex\"\n"
                + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/election/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isOk())
                .andExpect(result1 -> assertEquals(result1.getResponse().getContentAsString(),
                        "User: vladi voted for applicant: alex in election: battleROyal"));
    }

    @Test
    public void electionVoteTest2() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        Vote newVote = new TypelessVote("vladi");
        ElectionVote vote = new ElectionVote(newVote, "battleROyal", "alex");

        HoaId smg = new HoaId("SMG");
        Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        Address address = new Address("Bulgaria", "Sofia", "Street", 12, "1303");
        MemberAppUser member = new MemberAppUser("vladi", smgHoa, address);
        member.setRole("APPLICANT");
        membersRepository.save(member);
        MemberAppUser member2 = new MemberAppUser("rafa", smgHoa, address);
        membersRepository.save(member2);
        MemberAppUser member3 = new MemberAppUser("alex", smgHoa, address);
        member3.setRole("APPLICANT");
        membersRepository.save(member3);
        MemberAppUser member4 = new MemberAppUser("roland", smgHoa, address);
        member4.setRole("APPLICANT");
        membersRepository.save(member4);
        MemberAppUser member5 = new MemberAppUser("jelt", smgHoa, address);
        membersRepository.save(member5);
        Election election = new Election("battleROyal", smg.toString());
        //election.updateVote(new ElectionVote(new TypelessVote("vladi"), "1", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("alex"), "battleROyal", "roland"));
        election.updateVote(new ElectionVote(new TypelessVote("rafa"), "battleROyal", "vladi"));
        election.updateVote(new ElectionVote(new TypelessVote("jelt"), "battleROyal", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("roland"), "battleROyal", "vladi"));
        electionRepository.save(election);

        String bodyRequest = "{\n"
                + "    \"electionId\": \"battleROyal\",\n"
                + "    \"hoaId\": \"SMG\",\n"
                + "    \"applicantId\": \"alex\"\n"
                + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/election/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                        .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isBadRequest())
                .andExpect(result1 -> assertTrue(result1.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result1 -> assertEquals(HttpStatus.BAD_REQUEST.toString() + " \""
                        + smgHoa.getHoaId().toString() + "\"", result1.getResolvedException().getMessage()));
    }

    @Test
    public void electionsResult1() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        HoaId smg = new HoaId("SMG");
        Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        rep.save(smgHoa);
        Address address = new Address("Bulgaria", "Sofia", "Street", 12, "1303");
        MemberAppUser member = new MemberAppUser("vladi", smgHoa, address);
        member.setBoardMember(true);
        membersRepository.save(member);
        MemberAppUser member2 = new MemberAppUser("rafa", smgHoa, address);
        member2.setBoardMember(true);
        membersRepository.save(member2);
        MemberAppUser member3 = new MemberAppUser("alex", smgHoa, address);
        member3.setBoardMember(true);
        membersRepository.save(member3);
        MemberAppUser member4 = new MemberAppUser("roland", smgHoa, address);
        membersRepository.save(member4);
        MemberAppUser member5 = new MemberAppUser("jelt", smgHoa, address);
        membersRepository.save(member5);
        Election election = new Election("1", smg.toString());
        election.updateVote(new ElectionVote(new TypelessVote("vladi"), "1", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("alex"), "1", "roland"));
        election.updateVote(new ElectionVote(new TypelessVote("rafa"), "1", "vladi"));
        election.updateVote(new ElectionVote(new TypelessVote("jelt"), "1", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("roland"), "1", "vladi"));
        electionRepository.save(election);

        String bodyRequest = "{\n"
                + "    \"electionId\": \"1\",\n"
                + "    \"hoaId\": \"SMG\""
                + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/election/result")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest)
                .header("Authorization", "Bearer MockedToken"));
        result.andExpect(status().isOk());
        String output = "Hoa board has been updated with the following winners: ";
        List<MemberAppUser> board = electionRepository.findByBoard(smg);
        output += List.of(member.getNetId().toString(),
                member3.getNetId().toString(),
                member4.getNetId().toString()).toString();
        assertEquals(result.andReturn().getResponse().getContentAsString(), output);
    }

    @Test
    public void electionsResult2() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("vladi");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("vladi");
        HoaId smg = new HoaId("SMG");
        Hoa smgHoa = new Hoa(smg, "Bulgaria", "Sofia");
        rep.save(smgHoa);
        Address address = new Address("Bulgaria", "Sofia", "Street", 12, "1303");
        MemberAppUser member = new MemberAppUser("vladi", smgHoa, address);
        member.setBoardMember(true);
        membersRepository.save(member);
        MemberAppUser member2 = new MemberAppUser("rafa", smgHoa, address);
        member2.setBoardMember(true);
        membersRepository.save(member2);
        MemberAppUser member3 = new MemberAppUser("alex", smgHoa, address);
        member3.setBoardMember(true);
        membersRepository.save(member3);
        MemberAppUser member4 = new MemberAppUser("roland", smgHoa, address);
        membersRepository.save(member4);
        MemberAppUser member5 = new MemberAppUser("jelt", smgHoa, address);
        membersRepository.save(member5);
        Election election = new Election("1", smg.toString());
        election.updateVote(new ElectionVote(new TypelessVote("vladi"), "1", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("alex"), "1", "roland"));
        election.updateVote(new ElectionVote(new TypelessVote("rafa"), "1", "vladi"));
        election.updateVote(new ElectionVote(new TypelessVote("jelt"), "1", "alex"));
        election.updateVote(new ElectionVote(new TypelessVote("roland"), "1", "vladi"));
        //electionRepository.save(election);

        String bodyRequest = "{\n"
                + "    \"electionId\": \"1\",\n"
                + "    \"hoaId\": \"SMG\""
                + "}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/election/result")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest)
                .header("Authorization", "Bearer MockedToken"))
                .andExpect(status().isBadRequest())
                .andExpect(result1 -> assertTrue(result1.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result1 -> assertEquals(HttpStatus.BAD_REQUEST.toString() + " \""
                        + election.getElectionId() + "\"", result1.getResolvedException().getMessage()));
    }
}