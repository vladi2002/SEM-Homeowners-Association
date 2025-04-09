package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import nl.tudelft.sem.hoa.domain.hoa.RuleHoa;
import nl.tudelft.sem.hoa.domain.hoa.RuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class RuleRepositoryTest {

    @Autowired
    RuleRepository rules;

    @Test
    public void repTest() {
        RuleHoa test1 = new RuleHoa(150, "HOA1234", "You shall not pass!");

        rules.save(test1);

        RuleHoa test2 = new RuleHoa(200, "HOA12347", "You shall not pass!9");

        rules.save(test2);

        assertThat(rules.findRulesByHoaId("HOA1234").size()).isEqualTo(1);

        assertThat(rules.findAll().size()).isEqualTo(2);
        assertThat(rules.findRulesByIdAndHoaId(150, "HOA1234").get(0).getRuleText()).isEqualTo("You shall not pass!");
        assertThat(rules.findRulesByIdAndHoaId(150, "HOA1234").size()).isEqualTo(1);
        rules.delete(test1);
        assertThat(rules.findAll().size()).isEqualTo(1);
        assertThat(rules.findRulesByIdAndHoaId(150, "HOA1234").isEmpty()).isTrue();
    }
}
