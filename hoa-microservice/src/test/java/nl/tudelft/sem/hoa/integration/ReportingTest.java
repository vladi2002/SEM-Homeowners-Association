package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.hoa.authentication.AuthManager;
import nl.tudelft.sem.hoa.authentication.JwtTokenVerifier;
import nl.tudelft.sem.hoa.domain.ReportRepository;
import nl.tudelft.sem.hoa.domain.ReportService;
import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import nl.tudelft.sem.hoa.domain.hoa.RuleHoa;
import nl.tudelft.sem.hoa.domain.hoa.RuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
@AutoConfigureMockMvc
public class ReportingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Autowired
    RuleRepository rules;

    @Autowired
    ReportRepository reports;

    @Autowired
    MembersRepository members;

    @Autowired
    ReportService reportService;

    @BeforeEach
    void setUp() {
        Hoa hoa1 = new Hoa(new HoaId("Hoa1"), "Netherland", "Den Haag");
        Hoa hoa2 = new Hoa(new HoaId("Hoa2"), "Netherland", "Den Haag");
        MemberAppUser test1 = new MemberAppUser("User1", hoa1, new Address("Netherlands", "Den Haag",
                "Dk Plein", 69, "532CK"));

        MemberAppUser test2 = new MemberAppUser("User2", hoa1, new Address("Netherlands", "Den Haag",
                "Dk Plein", 25, "532CK"));

        MemberAppUser test3 = new MemberAppUser("User3", hoa2, new Address("Netherlands", "Den Haag",
                "Dk Straat", 61, "532CK"));
        members.save(test1);
        members.save(test2);
        RuleHoa rule1 = new RuleHoa(1, "Hoa1", "Thy shall listen");

        RuleHoa rule2 = new RuleHoa(2, "Hoa1", "Thy shall quit");
        rules.save(rule1);

        rules.save(rule2);

    }

    @Test
    public void fileValidReport() throws Exception {

        when(mockAuthenticationManager.getNetId()).thenReturn("User2");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("User2");

        String bodyRequest = "{\n"
                + "    \"idAccused\": \"User1\",\n"
                + "    \"ruleId\": 1, \n"
                + "    \"reportText\": \"Rule violation\"\n"
                + "}";

        ResultActions result = mockMvc.perform(post("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken")
                .content(bodyRequest));

        // Assert
        result.andExpect(status().isOk());
        assertThat(reports.findReportByIdReporter("User2").get(0).getReportText()).isEqualTo("Rule violation");

    }

    @Test
    public void fileInvalidReport() throws Exception {

        when(mockAuthenticationManager.getNetId()).thenReturn("User3");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("User3");

        String bodyRequest = "{\n"
                + "    \"idAccused\": \"User2\",\n"
                + "    \"ruleId\": 1\n"
                + "    \"ruleText\": \"Rule violation\",\n"
                + "}";


        ResultActions result = mockMvc.perform(post("/report")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken")
                .content(bodyRequest));

        // Assert
        result.andExpect(status().isBadRequest());



    }
}
