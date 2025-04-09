package nl.tudelft.sem.hoa.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import nl.tudelft.sem.hoa.authentication.AuthManager;
import nl.tudelft.sem.hoa.domain.JoinHoaService;
import nl.tudelft.sem.hoa.domain.elections.ElectionCreationModel;
import nl.tudelft.sem.hoa.domain.elections.ElectionResultModel;
import nl.tudelft.sem.hoa.domain.elections.ElectionService;
import nl.tudelft.sem.hoa.domain.elections.VoteCreationModel;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.vote.ElectionVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import nl.tudelft.sem.hoa.domain.vote.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ElectionController {

    private final transient ElectionService electionService;
    private final transient JoinHoaService joinHoaService;
    private final transient AuthManager authManager;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     * @param electionService the election service
     * @param joinHoaService the join hoa service
     */
    @Autowired
    public ElectionController(ElectionService electionService,
                              JoinHoaService joinHoaService,
                              AuthManager authManager) {
        this.electionService = electionService;
        this.joinHoaService = joinHoaService;
        this.authManager = authManager;
    }

    /**
     * An endpoint to create an election in a hoa.
     *
     * @param request the election creation model
     * @return 200 Ok if the creation is successful, BAD_REQUEST if not
     */
    @PostMapping("/election/create")
    public ResponseEntity<String> createElection(@RequestBody ElectionCreationModel request) {
        String electionId;
        String hoaId;
        try {
            electionId = request.getElectionId();
            hoaId = request.getHoaId();
            electionService.createElection(electionId, hoaId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok("Election: " + electionId + "created in hoa: " + hoaId);
    }

    /**
     * Allows users to vote on an applicant in board elections.
     *
     * @param request the vote creation model
     * @return 200 Ok if the vote was successful
     */
    @PostMapping("/election/vote")
    public ResponseEntity<String> voteElection(@RequestBody VoteCreationModel request) {
        String electionId;
        String hoaId;
        String userId;
        String applicantId;
        try {
            electionId = request.getElectionId();
            hoaId = request.getHoaId();
            userId = authManager.getNetId();
            applicantId = request.getApplicantId();
            Vote newVote = new TypelessVote(userId);
            electionService.vote(new ElectionVote(newVote, electionId, applicantId), hoaId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok("User: " + userId + " voted for applicant: " + applicantId + " in election: " + electionId);
    }

    /**
     * closes an election and promotes the winners to the board.
     *
     * @param request the election closing model
     * @return 200 Ok if the election was closed successfully
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @PostMapping("/election/result")
    public ResponseEntity<String> getElectionResult(@RequestBody ElectionResultModel request) {
        List<String> winners;
        try {
            winners = electionService.electionResult(request.getElectionId(), request.getHoaId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok("Hoa board has been updated with the following winners: " + winners.toString());
    }

    /**
     * Mapping to apply for board.
     *
     * @return output whether the applying worked or not
     */
    @PostMapping("/apply-board")
    public ResponseEntity<String> applyToBoard() {
        try {
            MemberAppUser member = joinHoaService.findMember(authManager.getNetId());
            System.out.println(member.toString());

            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
            String currentTime = LocalDateTime.now().format(myFormatObj);

            if (!member.getBoardMember()) {
                //not board
                //I set up the time required to 10 seconds instead of 3 years
                boolean time = Math.abs(Integer.parseInt(member.getTime().split("/")[1].split(":")[2])
                        - Integer.parseInt(currentTime.split("/")[1].split(":")[2])) > 10;
                System.out.println((Integer.parseInt(member.getTime().split("/")[1].split(":")[2])
                        - Integer.parseInt(currentTime.split("/")[1].split(":")[2])));
                if (time) {
                    joinHoaService.updateRoleMember(member, "APPLICANT");

                } else {
                    System.out.println("Not enough time in the HOA!");
                    return ResponseEntity.badRequest().build();
                }
            } else {
                //already board
                //I set up the time required to 15 seconds instead of 10 years
                boolean time = Math.abs(Integer.parseInt(member.getTime().split("/")[1].split(":")[2])
                        - Integer.parseInt(currentTime.split("/")[1].split(":")[2])) < 15;
                if (time) {
                    joinHoaService.updateRoleMember(member, "APPLICANT");
                } else {
                    System.out.println("Member has been in the board for too long!");
                    return ResponseEntity.badRequest().build();
                }
            }
            return ResponseEntity.ok("The user successfully applied to the board");
        } catch (Exception e) {
            System.out.println("The user does not belong to an HOA!");
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Finds board members in an HOA.
     *
     * @param hoa hoa
     * @return list of members
     */
    @GetMapping("/find-board/{hoa}")
    public ResponseEntity<String> findBoardMembers(@PathVariable String hoa) {
        try {
            List<MemberAppUser> members = electionService.findByBoard(new HoaId(hoa));
            String output = "";

            for (MemberAppUser m : members) {
                output += m.toString();
                output += "\n";
            }
            return ResponseEntity.ok(output);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
