package nl.tudelft.sem.hoa.domain.hoa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;




@Repository
public interface RuleRepository extends JpaRepository<RuleHoa, Integer> {

    List<RuleHoa> findRulesByHoaId(String hoaId);

    @Query("SELECT a FROM RuleHoa a WHERE (a.ruleId = ?1 AND a.hoaId=?2) ")
    List<RuleHoa> findRulesByIdAndHoaId(int ruleId, String hoaId);
}
