package nl.tudelft.sem.hoa.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import nl.tudelft.sem.hoa.authentication.AuthManager;
import nl.tudelft.sem.hoa.domain.JoinHoaService;
import nl.tudelft.sem.hoa.domain.Report;
import nl.tudelft.sem.hoa.domain.ReportService;
import nl.tudelft.sem.hoa.domain.elections.ElectionCreationModel;
import nl.tudelft.sem.hoa.domain.elections.ElectionResultModel;
import nl.tudelft.sem.hoa.domain.elections.ElectionService;
import nl.tudelft.sem.hoa.domain.elections.VoteCreationModel;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.RuleHoa;
import nl.tudelft.sem.hoa.domain.hoa.RuleService;
import nl.tudelft.sem.hoa.domain.vote.ElectionVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import nl.tudelft.sem.hoa.domain.vote.Vote;
import nl.tudelft.sem.hoa.models.ReportUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Hoa controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class DefaultController {

    private final transient AuthManager authManager;

    private final transient JoinHoaService joinHoaService;

    private final transient ReportService reportService;

    private final transient RuleService ruleService;


    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public DefaultController(AuthManager authManager,
                             JoinHoaService joinHoaService,
                             ReportService reportService, RuleService ruleService) {
        this.authManager = authManager;
        this.joinHoaService = joinHoaService;
        this.reportService = reportService;
        this.ruleService = ruleService;
    }



    /**
     * Endpoint to report a user.
     *
     * @param request Reuest to report a user
     * @return ok string.
     */
    @PostMapping("/report")
    public ResponseEntity<String> reportUser(@RequestBody ReportUserModel request) {
        try {
            String netId = authManager.getNetId();
            HoaId hoaId = joinHoaService.findHoa(netId);
            reportService.reportUser(netId, request.getIdAccused(), request.getRuleId(),
                hoaId.toString(), request.getReportText());
            return ResponseEntity.ok("Report successfull");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Report");
        }
    }

    /**
     * Boardmembers can acces all reports of an hoa.
     *
     * @return List of reports of the hoa
     */
    @GetMapping("/get-reports")
    public ResponseEntity<List<Report>> getReports() {
        // board members can see the reports of the entire HOA
        try {
            String netId = authManager.getNetId();
            return ResponseEntity.ok(reportService.getReportsHoa(netId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a member of the board!");
        }
    }

    /**
     * Members of an hoa can access all the rules.
     *
     * @return List of rules of the hoa
     */
    @GetMapping("/get-rules")
    public ResponseEntity<List<RuleHoa>> getRules() {

        try {
            String netId = authManager.getNetId();
            return ResponseEntity.ok(ruleService.getRulesByHoa(
                    joinHoaService.findHoa(netId).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a member of an HOA!");
        }
    }

}

