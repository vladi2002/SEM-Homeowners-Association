package nl.tudelft.sem.hoa.domain.elections;

import java.util.List;
import nl.tudelft.sem.hoa.domain.ElectionExceptionService;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import nl.tudelft.sem.hoa.domain.vote.ElectionVote;
import org.springframework.stereotype.Service;




@Service
public class ElectionService {
    private final transient ElectionRepository electionRepository;
    private final transient HoaRepository hoaRepository;
    private final transient MembersRepository membersRepository;

    /**
     * Instantiates a new ElectionService.
     *
     * @param electionRepository the election repository
     * @param hoaRepository the hoa repository
     * @param membersRepository the members repository
     */
    public ElectionService(ElectionRepository electionRepository,
                           HoaRepository hoaRepository, MembersRepository membersRepository) {
        this.electionRepository = electionRepository;
        this.hoaRepository = hoaRepository;
        this.membersRepository = membersRepository;
    }

    /**
     * creates an election and saves it in the database.
     *
     * @param electionId the string id of the election
     * @param hoaId the id of the hoa where the election takes place
     * @throws Exception if an election with the same electionId exists
     */
    public void createElection(String electionId, String hoaId) throws Exception {
        if (electionRepository.existsByElectionId(electionId)) {
            ElectionExceptionService.electionIdAlreadyExists(electionId);
        }
        if (!hoaRepository.existsByHoaId(new HoaId(hoaId))) {
            ElectionExceptionService.hoaDoesNotExist(hoaId);
        }
        electionRepository.save(new Election(electionId, hoaId));
    }

    /**
     * Updates the vote in the election if the vote is valid.
     *
     * @param vote the vote to be added to the election
     * @param hoaId the hoa in which the election takes place
     * @throws Exception if the vote isn't valid
     */
    public void vote(ElectionVote vote, String hoaId) throws Exception {
        if (vote.getApplicantId().equals(vote.getUserId())) {
            ElectionExceptionService.cantVoteForThemselves(vote.getUserId());
        }
        if (!hoaRepository.existsByHoaId(new HoaId(hoaId))) {
            ElectionExceptionService.hoaDoesNotExist(hoaId);
        }
        if (!electionRepository.existsByElectionId(vote.getElectionId())) {
            ElectionExceptionService.electionDoesntExist(vote.getElectionId());
        }
        MemberAppUser applicant = membersRepository.findMemberAppUsersByUsername(vote.getApplicantId()).get();
        if (!applicant.getRole().equals("APPLICANT")) {
            ElectionExceptionService.applicantDoesNotExist(vote.getElectionId());
        }

        if (!applicant.getHoaId().toString().equals(hoaId)) {
            ElectionExceptionService.applicantDoesNotExist(vote.getElectionId());
        }

        Election election = electionRepository.findByElectionId(vote.getElectionId()).get();
        if (election.userVoted(vote.getUserId())) {
            ElectionExceptionService.userHasAlreadyVoted(vote.getElectionId());
        }
        election.updateVote(vote);
        electionRepository.save(election);
    }

    /**
     * promotes the applicants with the most votes to a boardmember.
     *
     * @param electionId the id of the election
     * @param hoaId the id of the hoa
     * @throws Exception if the election or the hoa doesn't exist
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public List<String> electionResult(String electionId, String hoaId) throws Exception {
        if (!electionRepository.existsByElectionId(electionId)) {
            ElectionExceptionService.electionDoesntExist(electionId);
        }
        Election election = electionRepository.findByElectionId(electionId).get();
        if (!hoaRepository.existsByHoaId(new HoaId(hoaId))) {
            ElectionExceptionService.hoaDoesNotExist(hoaId);
        }
        if (!election.getHoaId().equals(hoaId)) {
            ElectionExceptionService.hoaDoesNotExist(hoaId);
        }
        List<String> result = election.getResult();
        if (result == null) {
            ElectionExceptionService.electionHasNoVotes(electionId);
        }

        //remove boardmembers from the board
        List<MemberAppUser> boardMembers = electionRepository.findByBoard(new HoaId(hoaId));
        for (MemberAppUser boardMember : boardMembers) {
            boardMember.setBoardMember(false);
            membersRepository.save(boardMember);
        }

        //add the new boardmembers to the board
        for (String boardMember : result) {
            MemberAppUser member = membersRepository.findMemberAppUsersByUsername(boardMember).get();
            member.setBoardMember(true);
            membersRepository.save(member);
        }

        //remove the election from the database
        electionRepository.delete(election);
        return result;
    }

    /**
     * Finds members of the board.
     *
     * @param hoaId id of the HOA
     * @return list of members
     * @throws Exception exception
     */
    public List<MemberAppUser> findByBoard(HoaId hoaId) throws Exception {
        return electionRepository.findByBoard(hoaId);
    }

}
