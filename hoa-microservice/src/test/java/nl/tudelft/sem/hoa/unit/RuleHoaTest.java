package nl.tudelft.sem.hoa.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.hoa.domain.hoa.RuleHoa;
import org.junit.jupiter.api.Test;


class RuleHoaTest {





    @Test
    void getRuleText() {
        RuleHoa rule1 = new RuleHoa(35, "Testoa", "You shall listen to thy neighbor");

        RuleHoa rule2 = new RuleHoa(23, "", "");

        assertThat(rule1.getRuleText()).isEqualTo("You shall listen to thy neighbor");

        assertThat(rule2.getRuleText()).isEqualTo("");

    }

    @Test
    void getHoaId() {
        RuleHoa rule1 = new RuleHoa(65, "Testoa", "You shall listen to thy neighbor");

        RuleHoa rule2 = new RuleHoa(23, "", "");

        assertThat(rule1.getHoaId()).isEqualTo("Testoa");

        assertThat(rule2.getHoaId()).isEqualTo("");
    }




    @Test
    void testEquals() {
        RuleHoa rule1 = new RuleHoa(23, "Testoa", "You shall listen to thy neighbor");

        RuleHoa rule2 = new RuleHoa(45, "Testoa", "");
        assertThat(rule1).isNotEqualTo(rule2);

        RuleHoa rule3 = new RuleHoa(23, "Testoa", "You shall listen to thy neighbor");
        assertThat(rule1).isEqualTo(rule3);

        RuleHoa rule4 = new RuleHoa(90, "Testoa", "You shall listen to thy neighbor ");
        assertThat(rule4).isNotEqualTo(rule1);

        RuleHoa rule5 = rule1;
        assertThat(rule1).isEqualTo(rule5);
    }

    @Test
    public void testEqualsFalse() {
        RuleHoa rule1 = new RuleHoa(23, "Testoa", "You shall listen to thy neighbor");
        RuleHoa rule2 = new RuleHoa(22, "Testoa", "You shall listen to thy neighbor");
        assertThat(rule1).isNotEqualTo(rule2);

        RuleHoa rule3 = new RuleHoa(23, "Tesoa", "You shall listen to thy neighbor");
        assertThat(rule1).isNotEqualTo(rule3);

        RuleHoa rule4 = new RuleHoa(23, "Testoa", "You shal listen to thy neighbor");
        assertThat(rule1).isNotEqualTo(rule4);

        Object none = null;
        assertThat(rule1).isNotEqualTo(none);

        Object different = new String("Invalid");
        assertThat(rule1).isNotEqualTo(different);
    }

    @Test
    public void testHashCode() {
        final RuleHoa rule1 = new RuleHoa(23, "Testoa", "You shall listen to thy neighbor");
        final RuleHoa rule2 = new RuleHoa(23, "Testoa", "You shall listen to thy neighbor");
        assertThat(rule1.hashCode()).isEqualTo(rule2.hashCode());
    }
}