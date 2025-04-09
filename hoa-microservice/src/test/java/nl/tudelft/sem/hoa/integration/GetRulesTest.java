package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import nl.tudelft.sem.hoa.domain.hoa.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class GetRulesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Autowired
    RuleRepository rules;



    @Autowired
    MembersRepository members;

    @Autowired
    RuleService ruleService;

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
        members.save(test3);
        RuleHoa rule1 = new RuleHoa(1, "Hoa1", "Thy shall listen");

        RuleHoa rule2 = new RuleHoa(2, "Hoa1", "Thy shall quit");
        RuleHoa rule3 = new RuleHoa(2, "Hoa2", "Thy shall quit");
        rules.save(rule1);

        rules.save(rule2);
        rules.save(rule3);

    }

    @Test
    public void getRulesValid1() throws Exception {

        when(mockAuthenticationManager.getNetId()).thenReturn("User1");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("User1");

        String id1 = String.valueOf(rules.findRulesByIdAndHoaId(1, "Hoa1").get(0).getId());
        String id2 = String.valueOf(rules.findRulesByIdAndHoaId(2, "Hoa1").get(0).getId());
        String expectedResponse = "[{\"id\":" + id1 + ",\"ruleId\":1,\"hoaId\":\"Hoa1\",\"ruleText\":"
                + "\"Thy shall listen\"},{\"id\":" + id2 + ",\"ruleId\":2,"
                + "\"hoaId\":\"Hoa1\",\"ruleText\":\"Thy shall quit\"}]";

        ResultActions result = mockMvc.perform(get("/get-rules")
                .header("Authorization", "Bearer MockedToken"));


        // Assert
        result.andExpect(status().isOk());
        //assertThat(result.toString()).isEqualTo("org.springframework.test.web.servlet.MockMvc$1@30cd9010");
        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo(expectedResponse);

    }

    @Test
    public void getRulesValid2() throws Exception {



        when(mockAuthenticationManager.getNetId()).thenReturn("User3");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("User3");

        ResultActions result2 = mockMvc.perform(get("/get-rules")
                .header("Authorization", "Bearer MockedToken"));

        String id1 = String.valueOf(rules.findRulesByIdAndHoaId(2, "Hoa2").get(0).getId());

        String expectedResponse2 = "[{\"id\":" + id1 + ",\"ruleId\":2,\"hoaId\""
                + ":\"Hoa2\",\"ruleText\":\"Thy shall quit\"}]";
        result2.andExpect(status().isOk());

        String response2 = result2.andReturn().getResponse().getContentAsString();
        assertThat(response2).isEqualTo(expectedResponse2);


    }

    @Test
    public void getRulesInvalid() throws Exception {



        when(mockAuthenticationManager.getNetId()).thenReturn("Doerf");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("Doerf");

        ResultActions result = mockMvc.perform(get("/get-rules")
                .header("Authorization", "Bearer MockedToken"));

        result.andExpect(status().isBadRequest());



    }


}
