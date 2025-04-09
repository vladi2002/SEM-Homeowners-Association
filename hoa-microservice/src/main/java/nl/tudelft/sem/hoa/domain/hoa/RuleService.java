package nl.tudelft.sem.hoa.domain.hoa;

import java.util.List;
import org.springframework.stereotype.Service;




@Service
public class RuleService {

    private final transient RuleRepository ruleRepository;

    private final transient HoaRepository hoaRepository;

    private final transient MembersRepository membersRepository;

    /**
     * Creates a new rule Service.
     *
     *  @param ruleRepository Repository of all the rules.
     * @param hoaRepository Repository of all hoas.
     * @param membersRepository repository of all Members.
     */
    public RuleService(RuleRepository ruleRepository, HoaRepository hoaRepository, MembersRepository membersRepository) {

        this.ruleRepository = ruleRepository;

        this.hoaRepository = hoaRepository;
        this.membersRepository = membersRepository;
    }

    /**
     * Adds a new Rule.
     *
     * @param hoaId String of the HOA Id.
     * @param ruleText Text of the rule.
     * @return returns the rule that was added.
     * @throws Exception Throws an exception if there is no rule text or  the HOA does not exist.
     */
    public RuleHoa addRule(int ruleId, String hoaId, String ruleText) throws Exception {

        if (!hoaRepository.existsByHoaId(new HoaId(hoaId))) {

            throw new HoaDoesNotExistException(hoaId);
        }
        if (ruleText == null || ruleText.isEmpty()) {
            throw new Exception();
        }
        if (!ruleRepository.findRulesByIdAndHoaId(ruleId, hoaId).isEmpty()) {
            throw new Exception();
        }
        RuleHoa rule = new RuleHoa(ruleId, hoaId, ruleText);

        ruleRepository.save(rule);

        return rule;
    }

    /**
     * Deletes a rule.
     *
     * @param ruleId Id of the rule that should be deleted
     * @return returns if deleting was successfull
     */
    public  boolean deleteRule(int ruleId, String hoaId) {

        if (ruleRepository.findRulesByIdAndHoaId(ruleId, hoaId).isEmpty()) {

            return false;
        } else {

            RuleHoa toBeDeleted = ruleRepository.findRulesByIdAndHoaId(ruleId, hoaId).get(0);
            ruleRepository.delete(toBeDeleted);

            return true;
        }
    }

    /**
     * Returns the rules of an HOA.
     *
     * @param hoaId Id of the hoa.
     * @return returns the list of rules for an HOA.
     */

    public List<RuleHoa> getRulesByHoa(String hoaId) {


        return ruleRepository.findRulesByHoaId(hoaId);
    }
}
