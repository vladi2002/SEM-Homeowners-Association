package nl.tudelft.sem.hoa.domain;

import java.util.List;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import nl.tudelft.sem.hoa.domain.hoa.RuleRepository;
import org.springframework.stereotype.Service;


@Service
public class ReportService {

    private final transient ReportRepository reportRepository;

    private final transient MembersRepository membersRepository;

    private final transient RuleRepository rulesRepository;

    /**
     *Constructor of Report service.
     *
     * @param reportRepository The repository for all reports.
     * @param membersRepository Reposity of all memebrs
     * @param rulesRepository Repository of all rules.
     */
    public ReportService(ReportRepository reportRepository,
                         MembersRepository membersRepository, RuleRepository rulesRepository) {
        this.reportRepository = reportRepository;
        this.membersRepository = membersRepository;
        this.rulesRepository = rulesRepository;
    }

    /**
     * File a new Report.
     *
     * @param idReporter NetId of the reporter
     * @param idAccused NetId of the accused.
     * @param ruleId RuleId of the broken rule.
     * @param hoaId HoaId of the Hoa.
     * @param reportText Report Text.
     * @return Returns the Report.
     * @throws Exception Throws exception if invalid report.
     */
    public Report reportUser(String idReporter, String idAccused,
                             int ruleId, String hoaId, String reportText) throws Exception {
        if (membersRepository.findMemberAppUsersByUsername(idAccused).isEmpty()
            || !membersRepository.findMemberAppUsersByUsername(idAccused).get().getHoaId().equals(new HoaId(hoaId))) {
            throw new Exception();
        }
        if (rulesRepository.findRulesByIdAndHoaId(ruleId, hoaId).isEmpty() || reportText == null) {
            throw new Exception();
        }
        Report report = new Report(idReporter, idAccused, ruleId, hoaId, reportText);
        reportRepository.save(report);
        return report;
    }

    /**
     * Allows boardmembers to see all the reports in the HOA.
     *
     * @param netId netid of the user
     * @return  returns list of
     * @throws Exception User is not a boardmember.
     */
    public List<Report> getReportsHoa(String netId) throws Exception {
        MemberAppUser user = membersRepository.findMemberAppUsersByUsername(netId).get();
        if (user.getBoardMember()) {
            return reportRepository.findReportByHoaId(user.getHoaId().toString());
        }
        throw new Exception("Not a boardmember!");
    }

    public List<Report> getReportsByIdReporter(String idReporter) {
        return reportRepository.findReportByIdReporter(idReporter);
    }

    public List<Report> getReportsByIdAccused(String idAccused) {
        return reportRepository.findReportByIdAccused(idAccused);
    }
}
