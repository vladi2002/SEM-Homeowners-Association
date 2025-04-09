package nl.tudelft.sem.hoa.domain.proposals;

import java.util.ArrayList;
import nl.tudelft.sem.hoa.domain.HoaIdNotFoundException;
import nl.tudelft.sem.hoa.domain.ProposalExceptionService;
import nl.tudelft.sem.hoa.domain.RuleNotFoundException;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import nl.tudelft.sem.hoa.domain.hoa.RuleRepository;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.AlreadyVotedException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.InvalidRuleIdException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.NotBoardMemberException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.NotPartOfHoaException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalIdAlreadyExistsException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalIdNotFoundException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalVotingIsClosedException;
import nl.tudelft.sem.hoa.domain.vote.ProposalVote;
import org.springframework.stereotype.Service;


@Service
public class ProposalService {

    private final transient ProposalRepository proposalRepository;
    private final transient HoaRepository hoaRepository;
    private final transient MembersRepository membersRepository;
    private final transient RuleRepository ruleRepository;

    /**
     * Instantiate a new proposal service.
     *
     * @param proposalRepository The proposal repository
     */
    public ProposalService(ProposalRepository proposalRepository,
                           HoaRepository hoaRepository,
                           MembersRepository membersRepository,
                           RuleRepository ruleRepository) {
        this.proposalRepository = proposalRepository;
        this.hoaRepository = hoaRepository;
        this.membersRepository = membersRepository;
        this.ruleRepository = ruleRepository;
    }

    /**
     * Create a new proposal and save it to the database.
     *
     * @param proposalPk The proposalID object
     * @param proposal The proposal
     * @param proposalType The type of the proposal
     * @param ruleId The ruleId of a rule proposal (unused if of type GENERAL, defaults to 0)
     * @throws Exception if proposalID already exists
     */
    public void createProposal(ProposalPk proposalPk, String proposal,
                               ProposalType proposalType, int ruleId, String netId) throws Exception {
        // Check if HOA exists and Proposal ID is unused
        HoaId hoaId = proposalPk.getHoaId();
        if (!hoaRepository.existsByHoaId(hoaId)) {
            ProposalExceptionService.hoaIdNotFound(hoaId);
        } else if (proposalRepository.existsByProposalPk(proposalPk)) {
            ProposalExceptionService.proposalIdAlreadyExists(proposalPk);
        }

        // Check if user is part of given HOA and is board member
        isBoardMemberFromHoa(hoaId, netId);

        // Save to db depending on type
        if (proposalType == ProposalType.GENERAL) {
            proposalRepository.save(new Proposal(proposalPk, proposal, proposalType, new ArrayList<>()));
        } else {
            if (ruleId == 0) {
                ProposalExceptionService.invalidRuleId(ruleId);
            } else if (ruleRepository.findRulesByIdAndHoaId(ruleId, hoaId.toString()).size() == 0) {
                ProposalExceptionService.ruleNotFound(ruleId);
            }
            proposalRepository.save(new RuleProposal(proposalPk, proposal, proposalType, new ArrayList<>(), ruleId));
        }
    }

    /**
     * Vote on a proposal and save to the database.
     *
     * @param vote The vote to be counted
     * @throws Exception if proposal ID does not exist, user has already voted or user is not a board member of the HOA
     */
    public void voteProposal(ProposalVote vote) throws Exception {
        ProposalPk proposalPk = vote.getProposalId();
        Proposal proposal = getProposal(proposalPk);
        if (proposal.hasVoted(vote.getUserId())) {
            ProposalExceptionService.alreadyVoted(proposal, vote.getUserId());
        }
        if (!proposal.getDate().twoWeeksPassed() || proposal.getDate().votingHasClosed()) {
            ProposalExceptionService.proposalVotingIsClosed(proposalPk);
        }
        isBoardMemberFromHoa(proposalPk.getHoaId(), vote.getUserId());
        proposal.updateVote(vote);
        proposalRepository.save(proposal);
    }

    /**
     * Getter for a proposal given proposalPk.
     *
     * @param proposalPk The proposal ID
     * @return A proposal object
     * @throws Exception if proposalPk does not exist
     */
    public Proposal getProposal(ProposalPk proposalPk) throws Exception {
        if (!proposalRepository.existsByProposalPk(proposalPk)) {
            ProposalExceptionService.proposalIdNotFound(proposalPk);
        }
        return proposalRepository.findProposalByProposalPk(proposalPk).orElseThrow();
    }

    /**
     * Check if a user is part of an HOA and a board member.
     *
     * @param hoaId The hoa ID
     * @param netId The net ID
     * @throws Exception if user is not a board member of the HOA
     */
    private void isBoardMemberFromHoa(HoaId hoaId, String netId) throws Exception {
        MemberAppUser memberAppUser = membersRepository.findMemberAppUsersByUsername(netId).orElseThrow();
        if (!memberAppUser.getHoaId().equals(hoaId)) {
            ProposalExceptionService.notPartOfHoa(netId, hoaId);
        } else if (!memberAppUser.getBoardMember()) {
            ProposalExceptionService.notBoardMember(memberAppUser);
        }
    }
}
