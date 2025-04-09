package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class ReportServiceTest {

    @Autowired
    RuleRepository rules;

    @Autowired
    ReportRepository reports;

    @Autowired
    MembersRepository members;

    @Autowired
    ReportService reportService;

    //@Autowired
    //MembersRepository membersRepository;// = Mockito.mock(MembersRepository.class);

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
    void testReportUser() throws Exception {


        reportService.reportUser("User2", "User1", 2, "Hoa1", "Blabla1");

        assertThat(reportService.getReportsByIdReporter("User2").get(0).getReportText()).isEqualTo("Blabla1");



        reportService.reportUser("User1", "User2", 2, "Hoa1", "Sruffjd");

        assertThat(reportService.getReportsByIdAccused("User2").get(0).getReportText()).isEqualTo("Sruffjd");
        assertThat(reportService.getReportsByIdAccused("User1").get(0).getReportText()).isEqualTo("Blabla1");

        assertThrows(Exception.class, () -> {
            reportService.reportUser("User2", "User3", 2, "Hoa2", "Sruffjd");
        });

        assertThrows(Exception.class, () -> {
            reportService.reportUser("User2", "XXXTE", 2, "Hoa1",
                null);
        });

        assertThrows(Exception.class, () -> {
            reportService.reportUser("User2", "User1", 2, "Hoa1",
                null);
        });

        assertThrows(Exception.class, () -> {
            reportService.reportUser("User2", "User1", 17, "Hoa1",
                "");
        });

        assertThrows(Exception.class, () -> {
            reportService.reportUser("User2", "User1", 17, "Hoa1",
                "User1 has a tree that is 1,5 cm too tall");
        });

        //assertThrows(Exception.class, () -> reportService.reportUser("User1", "User2", 1, "Hoa1", null));
    }

    @Test
    public void reportText_null_throws_correct_exception() {
        Exception exception = assertThrows(Exception.class, () -> reportService.reportUser("User1",
                "User2", 1, "Hoa1", null));

        assertTrue(exception.getClass() == Exception.class);
    }
}
