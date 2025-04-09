package nl.tudelft.sem.hoa.domain;



import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "idReporter", nullable = false)
    private  String idReporter;

    @Column(name = "idAccused", nullable = false)
    private  String idAccused;

    @Column(name = "ruleId", nullable = false)
    private  int ruleId;

    @Column(name = "hoaId", nullable = false)
    private String hoaId;

    @Column(name = "reportText", nullable = false)
    private String reportText;

    /**
     * Constructor of the report.
     *
     * @param idReporter NetId of person reporting.
     * @param idAccused NetId of person accused.
     * @param ruleId RUleid of rule violated.
     * @param hoaId Hoaid of the hoa both are in.
     * @param reportText Text of the report.
     */
    public Report(String idReporter, String idAccused, int ruleId, String hoaId, String reportText) {
        this.idReporter = idReporter;
        this.idAccused = idAccused;
        this.ruleId = ruleId;
        this.hoaId = hoaId;
        this.reportText = reportText;
    }

    public int getId() {
        return id;
    }

    public String getIdReporter() {
        return idReporter;
    }

    public String getIdAccused() {
        return idAccused;
    }

    public int getRuleId() {
        return ruleId;
    }

    public String getHoaId() {
        return hoaId;
    }

    public String getReportText() {
        return reportText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Report report = (Report) o;
        return id == report.id && ruleId == report.ruleId && Objects.equals(idReporter, report.idReporter)
            && Objects.equals(idAccused, report.idAccused) && Objects.equals(hoaId, report.hoaId)
            && Objects.equals(reportText, report.reportText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idReporter, idAccused, ruleId, hoaId, reportText);
    }
}
