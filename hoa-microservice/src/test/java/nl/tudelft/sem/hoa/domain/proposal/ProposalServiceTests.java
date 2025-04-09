package nl.tudelft.sem.hoa.domain.proposal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import nl.tudelft.sem.hoa.domain.CreationDate;
import nl.tudelft.sem.hoa.domain.HoaIdNotFoundException;
import nl.tudelft.sem.hoa.domain.RuleNotFoundException;
import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import nl.tudelft.sem.hoa.domain.hoa.RuleHoa;
import nl.tudelft.sem.hoa.domain.hoa.RuleRepository;
import nl.tudelft.sem.hoa.domain.proposals.Proposal;
import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;
import nl.tudelft.sem.hoa.domain.proposals.ProposalRepository;
import nl.tudelft.sem.hoa.domain.proposals.ProposalService;
import nl.tudelft.sem.hoa.domain.proposals.ProposalType;
import nl.tudelft.sem.hoa.domain.proposals.RuleProposal;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.AlreadyVotedException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.InvalidRuleIdException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.NotBoardMemberException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.NotPartOfHoaException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalIdAlreadyExistsException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalIdNotFoundException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalVotingIsClosedException;
import nl.tudelft.sem.hoa.domain.vote.Decision;
import nl.tudelft.sem.hoa.domain.vote.ProposalVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProposalServiceTests {

    @Autowired
    private transient ProposalService proposalService;

    @Autowired
    private transient ProposalRepository proposalRepository;

    @Autowired
    private transient HoaRepository hoaRepository;

    @Autowired
    private transient MembersRepository membersRepository;

    @Autowired
    private transient RuleRepository ruleRepository;

    @Test
    public void creation_works() throws Exception {
        // Arrange
        HoaId hoaId = new HoaId("hoaID");
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        String proposal = "This is a proposal.";

        Hoa hoa = setUpHoa(hoaId);
        setUpMember("netId", hoa, true);

        // Act
        proposalService.createProposal(proposalPk, proposal, ProposalType.GENERAL, 0, "netId");

        // Assert
        Proposal savedProposal = proposalRepository.findProposalByProposalPk(proposalPk).orElseThrow();

        Proposal expectedProposal = new Proposal(proposalPk, proposal, ProposalType.GENERAL,
            0, 0, 0, new ArrayList<>());

        assertThat(savedProposal)
            .isEqualTo(expectedProposal);
        assertThat(savedProposal.getAcceptVotes()).isEqualTo(0);
        assertThat(savedProposal.getRejectVotes()).isEqualTo(0);
        assertThat(savedProposal.getAbstainVotes()).isEqualTo(0);
    }

    @Test
    public void creation_throws_proposalAlreadyExists() throws Exception {
        // Arrange
        HoaId hoaId = new HoaId("hoaId");
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        String originalProposal = "This is a proposal.";
        String newProposal = "This is a new proposal.";

        Hoa hoa = setUpHoa(hoaId);
        setUpMember("netId", hoa, true);

        proposalService.createProposal(proposalPk, originalProposal, ProposalType.GENERAL, 0,  "netId");

        // Act and assert
        assertThrows(ProposalIdAlreadyExistsException.class, () ->
            proposalService.createProposal(proposalPk, newProposal, ProposalType.GENERAL, 0, "netId"));
    }

    @Test
    public void creation_throws_notBoardMember() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, false);

        assertThrows(NotBoardMemberException.class, () ->
            proposalService.createProposal(proposalPk, "This is a proposal.", ProposalType.GENERAL, 0, netId));
    }

    @Test
    public void creation_throws_notPartOfHoa() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);

        setUpHoa(hoaId);
        Hoa hoa = setUpHoa(new HoaId("newHoa"));
        setUpMember(netId, hoa, true);

        assertThrows(NotPartOfHoaException.class, () ->
            proposalService.createProposal(proposalPk, "Proposal", ProposalType.GENERAL, 0, netId));
    }

    @Test
    public void creation_throws_hoaNotFound() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);

        Hoa hoa = setUpHoa(new HoaId("newHoa"));
        setUpMember(netId, hoa, true);

        assertThrows(HoaIdNotFoundException.class, () ->
            proposalService.createProposal(proposalPk, "Pro", ProposalType.GENERAL, 0, netId));
    }

    @Test
    public void creation_with_rule_works() throws Exception {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, true);
        setUpRule(100, hoaId);

        proposalService.createProposal(proposalPk, "prop", ProposalType.RULE, 100, netId);

        RuleProposal proposal = (RuleProposal) proposalRepository.findProposalByProposalPk(proposalPk).orElseThrow();
        RuleProposal expected = new RuleProposal(proposalPk, "prop", ProposalType.RULE, new ArrayList<>(), 100);

        assertThat(proposal)
            .isEqualTo(expected);
    }

    @Test
    public void creation_throws_invalidRule() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, true);
        setUpRule(0, hoaId);

        Exception e = assertThrows(InvalidRuleIdException.class, () ->
            proposalService.createProposal(proposalPk, "prop", ProposalType.RULE, 0, netId));
        assertEquals(e.getMessage(), "Invalid rule ID.");
    }

    @Test
    public void creation_throws_ruleNotFound() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, true);

        assertThrows(RuleNotFoundException.class, () ->
            proposalService.createProposal(proposalPk, "prop", ProposalType.RULE, 1, netId));
    }

    @Test
    public void vote_works() throws Exception {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        Proposal proposal = new Proposal(proposalPk, "prop", ProposalType.GENERAL,
            new CreationDate(LocalDate.now().minusWeeks(2).minusDays(1)), new ArrayList<>());

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, true);

        proposalRepository.save(proposal);

        ProposalVote vote = new ProposalVote(new TypelessVote(netId), proposalPk, Decision.ACCEPT);
        proposalService.voteProposal(vote);

        Proposal savedProposal = proposalRepository.findProposalByProposalPk(proposalPk).orElseThrow();
        int[] expectedVotes = {1, 0, 0};
        assertThat(savedProposal.getVotes())
            .isEqualTo(expectedVotes);
    }

    @Test
    public void vote_throws_alreadyVoted() throws Exception {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        Proposal proposal = new Proposal(proposalPk, "prop", ProposalType.GENERAL,
            new CreationDate(LocalDate.now().minusWeeks(2).minusDays(1)), new ArrayList<>());

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, true);

        proposalRepository.save(proposal);

        ProposalVote vote = new ProposalVote(new TypelessVote(netId), proposalPk, Decision.ACCEPT);
        proposalService.voteProposal(vote);

        Exception e = assertThrows(AlreadyVotedException.class, () -> proposalService.voteProposal(vote));
        assertEquals(e.getMessage(), "User has already voted on this proposal.");
    }

    @Test
    public void vote_throws_votingClosedTooYoung() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        Proposal proposal = new Proposal(proposalPk, "prop", ProposalType.GENERAL,
            new CreationDate(LocalDate.now().minusWeeks(2)), new ArrayList<>());

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, true);

        proposalRepository.save(proposal);

        ProposalVote vote = new ProposalVote(new TypelessVote(netId), proposalPk, Decision.ACCEPT);
        assertThrows(ProposalVotingIsClosedException.class, () -> proposalService.voteProposal(vote));
    }

    @Test
    public void vote_throws_votingClosedTooOld() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        Proposal proposal = new Proposal(proposalPk, "prop", ProposalType.GENERAL,
            new CreationDate(LocalDate.now().minusWeeks(2).minusDays(4)), new ArrayList<>());

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, true);

        proposalRepository.save(proposal);

        ProposalVote vote = new ProposalVote(new TypelessVote(netId), proposalPk, Decision.ACCEPT);
        assertThrows(ProposalVotingIsClosedException.class, () -> proposalService.voteProposal(vote));
    }

    @Test
    public void vote_throws_notBoardMember() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        Proposal proposal = new Proposal(proposalPk, "prop", ProposalType.GENERAL,
            new CreationDate(LocalDate.now().minusWeeks(2).minusDays(1)), new ArrayList<>());

        Hoa hoa = setUpHoa(hoaId);
        setUpMember(netId, hoa, false);

        proposalRepository.save(proposal);

        ProposalVote vote = new ProposalVote(new TypelessVote(netId), proposalPk, Decision.ACCEPT);
        assertThrows(NotBoardMemberException.class, () -> proposalService.voteProposal(vote));
    }

    @Test
    public void vote_throws_notPartOfHoa() {
        HoaId hoaId = new HoaId("hoaId");
        String netId = "netId";
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        Proposal proposal = new Proposal(proposalPk, "prop", ProposalType.GENERAL,
            new CreationDate(LocalDate.now().minusWeeks(2).minusDays(1)), new ArrayList<>());

        setUpHoa(hoaId);
        Hoa hoa = setUpHoa(new HoaId("newHoa"));
        setUpMember(netId, hoa, false);

        proposalRepository.save(proposal);

        ProposalVote vote = new ProposalVote(new TypelessVote(netId), proposalPk, Decision.ACCEPT);
        assertThrows(NotPartOfHoaException.class, () -> proposalService.voteProposal(vote));
    }

    @Test
    public void get_works() throws Exception {
        HoaId hoaId = new HoaId("hoaId");
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);
        Proposal proposal = new Proposal(proposalPk, "prop", ProposalType.GENERAL,
            new CreationDate(LocalDate.now().minusWeeks(2).minusDays(1)), new ArrayList<>());

        proposalRepository.save(proposal);

        Proposal savedProposal = proposalService.getProposal(proposalPk);
        assertThat(savedProposal)
            .isEqualTo(proposal);
    }

    @Test
    public void get_throws_proposalNotFound() {
        HoaId hoaId = new HoaId("hoaId");
        ProposalPk proposalPk = new ProposalPk("proposalID", hoaId);

        assertThrows(ProposalIdNotFoundException.class, () -> proposalService.getProposal(proposalPk));
    }

    /**
     * Set up hoa.
     *
     * @param hoaId The hoa ID
     * @return An HOA
     */
    private Hoa setUpHoa(HoaId hoaId) {
        Hoa hoa = new Hoa(hoaId, "Country", "city");
        hoaRepository.save(hoa);
        return hoa;
    }

    /**
     * Set up a member.
     *
     * @param netId The net ID
     * @param hoa The HOA they belong to
     * @param isBoardMember True if they are a boardmember
     * @return A member
     */
    private MemberAppUser setUpMember(String netId, Hoa hoa, boolean isBoardMember) {
        MemberAppUser memberAppUser = new MemberAppUser(netId, hoa, new Address());
        memberAppUser.setBoardMember(isBoardMember);
        membersRepository.save(memberAppUser);
        return memberAppUser;
    }

    /**
     * Set up a rule.
     *
     * @param ruleId The rule ID
     * @param hoaId The hoa ID
     * @return A rule
     */
    private RuleHoa setUpRule(int ruleId, HoaId hoaId) {
        RuleHoa rule = new RuleHoa(ruleId, hoaId.toString(), "Some text");
        ruleRepository.save(rule);
        return rule;
    }
}
