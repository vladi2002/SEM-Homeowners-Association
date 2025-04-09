package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import nl.tudelft.sem.hoa.domain.Report;
import nl.tudelft.sem.hoa.domain.ReportRepository;
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
public class ReportRepositoryTest {

    @Autowired
    ReportRepository reports;

    @Test
    public void rulesRepTest() {
        Report test1 = new Report("User1", "User2", 25, "HOA187", "Mowing the lawn on sunday");

        reports.save(test1);
        Report test2 = new Report("User3", "User2", 85, "HOA187", "Rule violation");

        reports.save(test2);

        assertThat(reports.findReportByIdReporter("User1").get(0)).isEqualTo(test1);

        assertThat(reports.findReportByHoaId("HOA187").size()).isEqualTo(2);

        assertThat(reports.findReportByIdAccused("User2").size()).isEqualTo(2);

        assertThat(reports.findReportByIdAccused("User3").size()).isEqualTo(0);

        reports.delete(test1);

        assertThat(reports.findReportByIdAccused("User2").size()).isEqualTo(1);
    }
}
