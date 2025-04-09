package nl.tudelft.sem.hoa.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import nl.tudelft.sem.hoa.domain.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class ReportTest {


    Report test1 = new Report("User1", "User2", 25, "HOA187", "Mowing the lawn on sunday");


    Report test2 = new Report("User3", "User2", 85, "HOA3", "Rule violation");
    Report test3 = new Report("", "NULL", 0, "H", "Rule violation 2");



    @Test
    void getIdReporter() {
        assertThat(test1.getIdReporter()).isEqualTo("User1");

        assertThat(test2.getIdReporter()).isEqualTo("User3");

        assertThat(test3.getIdReporter()).isEqualTo("");
    }

    @Test
    void getIdAccused() {

        assertThat(test1.getIdAccused()).isEqualTo("User2");

        assertThat(test2.getIdAccused()).isEqualTo("User2");

        assertThat(test3.getIdAccused()).isEqualTo("NULL");
    }

    @Test
    void getRuleId() {
        assertThat(test1.getRuleId()).isEqualTo(25);

        assertThat(test2.getRuleId()).isEqualTo(85);

        assertThat(test3.getRuleId()).isEqualTo(0);
    }

    @Test
    void getHoaId() {
        assertThat(test1.getHoaId()).isEqualTo("HOA187");

        assertThat(test2.getHoaId()).isEqualTo("HOA3");

        assertThat(test3.getHoaId()).isEqualTo("H");
    }

    @Test
    void getReportText() {
        assertThat(test1.getReportText()).isEqualTo("Mowing the lawn on sunday");

        assertThat(test2.getReportText()).isEqualTo("Rule violation");

        assertThat(test3.getReportText()).isEqualTo("Rule violation 2");
    }

    @Test
    void testEquals() {
        Report test4 = new Report("", "NULL", 0, "H", "Rule violation 2");
        Report test5 = new Report(" ", "NULL", 0, "H", "Rule violation 2");
        Report test6 = new Report(" b", "NULL", 0, "H", "Rule violation 2");
        assertThat(test4).isEqualTo(test3);
        assertThat(test5).isNotEqualTo(test4);
        assertThat(test5).isNotEqualTo(test6);
        Report test7 = new Report(" ", "NLL", 0, "H", "Rule violation 2");
        assertThat(test5).isNotEqualTo(test7);
        Report test8 = new Report(" ", "NULL", 4, "H", "Rule violation 2");
        assertThat(test5).isNotEqualTo(test8);
        Report test9 = new Report(" ", "NULL", 0, "L", "Rule violation 2");
        assertThat(test5).isNotEqualTo(test9);
        Report test10 = new Report(" ", "NULL", 0, "H", "Rule vilation 2");
        assertThat(test5).isNotEqualTo(test10);
        Report none = null;
        Object invalid = new String("Invalid");
        assertThat(test5).isNotEqualTo(none);
        assertThat(test5).isNotEqualTo(invalid);
    }

    @Test
    void testHashCode() {
        Report test4 = new Report("", "NULL", 0, "H", "Rule violation 2");
        Report test5 = new Report(" ", "NULL", 0, "H", "Rule violation 2");

        assertThat(test4.hashCode()).isEqualTo(test3.hashCode());

        assertThat(test5.hashCode()).isNotEqualTo(test4.hashCode());

    }
}