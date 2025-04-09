package nl.tudelft.sem.hoa.domain.hoa;





import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.InvalidRuleIdException;


@Entity
@Table(name = "rules", uniqueConstraints = {@UniqueConstraint(columnNames = {"ruleId", "hoaId"})})
@NoArgsConstructor

public class RuleHoa {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "ruleId", nullable = false)
    private int ruleId;


    @Column(name = "HoaId", nullable = false)
    private String hoaId;


    @Column(name = "RuleText", nullable = false)
    private String ruleText;

    /**
     * Creates a new Rule.
     *
     * @param ruleId id of the rule
     * @param hoaId HOA id of the
     * @param ruleText Text of the rule.
     */
    public RuleHoa(int ruleId, String hoaId, String ruleText) {
        this.ruleId = ruleId;
        this.hoaId = hoaId;
        this.ruleText = ruleText;
    }

    public int getId() {
        return id;
    }

    public int getRuleId() {
        return ruleId;
    }

    public String getHoaId() {
        return hoaId;
    }

    public String getRuleText() {
        return ruleText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleHoa ruleHoa = (RuleHoa) o;
        return ruleId == ruleHoa.ruleId && Objects.equals(hoaId, ruleHoa.hoaId)
            && Objects.equals(ruleText, ruleHoa.ruleText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleId, hoaId, ruleText);
    }
}
