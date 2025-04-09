package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaDoesNotExistException;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.RuleRepository;
import nl.tudelft.sem.hoa.domain.hoa.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;



@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class RuleServiceTest {
    @Autowired
    HoaRepository hoaRepository;

    @Autowired
    RuleRepository rules;

    @Autowired
    RuleService ruleService;

    /**
     * SetUp.
     *
     */
    @BeforeEach
    public void setUp() {
        HoaId id = new HoaId("HOA12349");
        Hoa hoa = new Hoa(id, "EX", "TestCity");
        hoaRepository.save(hoa);
        HoaId id2 = new HoaId("HOA4321");
        Hoa hoa2 = new Hoa(id2, "EX", "TestCity");
        hoaRepository.save(hoa2);
    }

    @Test
    public void add_rules_Test() throws Exception {

        ruleService.addRule(143, "HOA12349", "You shall not pass!");
        assertThat(rules.findRulesByHoaId("HOA12349").get(0).getRuleText()).isEqualTo("You shall not pass!");


        ruleService.addRule(584, "HOA12349", "You shall not kill thy neighbor");
        assertThat(rules.findRulesByHoaId("HOA12349").get(1).getRuleText()).isEqualTo("You shall not kill thy neighbor");

        assertThrows(HoaDoesNotExistException.class, () -> {
            ruleService.addRule(234, "1234", "You shall not pass!");
        });

        assertThrows(Exception.class, () -> {
            ruleService.addRule(145, "HOA12349", "");
        });

        assertThrows(Exception.class, () -> {
            ruleService.addRule(523, "HOA12349", null);
        });

        assertThrows(Exception.class, () -> {
            ruleService.addRule(584, "HOA12349", " Hallo");
        });






    }

    @Test
    public void delete_rules_Test() throws Exception {

        ruleService.addRule(543, "HOA12349", "You shall not pass!");
        assertThat(rules.findRulesByHoaId("HOA12349").get(0).getRuleText()).isEqualTo("You shall not pass!");


        ruleService.addRule(154, "HOA12349", "You shall not kill thy neighbor");

        assertThat(rules.findRulesByHoaId("HOA12349").get(1).getRuleText()).isEqualTo("You shall not kill thy neighbor");


        assertThat(ruleService.deleteRule(154, "HOA12349")).isTrue();

        assertThat(ruleService.deleteRule(543, "HOA12349")).isTrue();

        assertThat(ruleService.deleteRule(-1234, "   ")).isFalse();
        assertThat(ruleService.deleteRule(543, "HOA12349")).isFalse();

        assertThat(rules.findRulesByIdAndHoaId(154, "HOA12349").isEmpty()).isTrue();



    }

    @Test
    public void findRulesByHoaId_test() throws Exception {

        assertThat(ruleService.getRulesByHoa("HOA12349"));
        ruleService.addRule(543, "HOA12349", "You shall not pass!");
        ruleService.addRule(542, "HOA12349", "You shall not pass either!");
        assertThat(ruleService.getRulesByHoa("HOA12349").size()).isEqualTo(2);

        assertThat(ruleService.getRulesByHoa("HOA12349").get(0).getRuleText()).isEqualTo("You shall not pass!");
        ruleService.addRule(542, "HOA4321", "Test3");
        assertThat(ruleService.getRulesByHoa("HOA4321").get(0).getRuleText()).isEqualTo("Test3");


    }
}
