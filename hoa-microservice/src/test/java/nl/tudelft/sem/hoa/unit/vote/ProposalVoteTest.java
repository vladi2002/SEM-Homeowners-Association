package nl.tudelft.sem.hoa.unit.vote;

import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;
import nl.tudelft.sem.hoa.domain.vote.Decision;
import nl.tudelft.sem.hoa.domain.vote.ProposalVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProposalVoteTest {

    @Test
    void getProposalIdNormal() {
        TypelessVote janVote = new TypelessVote("jan123");
        ProposalPk proposalPk = new ProposalPk("prop", new HoaId("hoa"));
        ProposalVote janVotesYes = new ProposalVote(janVote, proposalPk, Decision.ACCEPT);

        Assertions.assertEquals(janVotesYes.getProposalId(), proposalPk);
    }

    @Test
    void getProposalIdEmpty() {
        TypelessVote janVote = new TypelessVote("jan123");
        ProposalVote janVotesYes = new ProposalVote(janVote, null, Decision.ACCEPT);

        Assertions.assertEquals(janVotesYes.getProposalId(), null);
    }

    @Test
    void getDecisionAccept() {
        TypelessVote janVote = new TypelessVote("jan123");
        ProposalPk proposalPk = new ProposalPk("prop", new HoaId("hoa"));
        ProposalVote janVotesYes = new ProposalVote(janVote, proposalPk, Decision.ACCEPT);

        Assertions.assertEquals(janVotesYes.getDecision(), Decision.ACCEPT);
    }

    @Test
    void setDecisionChange() {
        TypelessVote janVote = new TypelessVote("jan123");
        ProposalPk proposalPk = new ProposalPk("prop", new HoaId("hoa"));
        ProposalVote janVotesYes = new ProposalVote(janVote, proposalPk, Decision.ACCEPT);

        Assertions.assertEquals(janVotesYes.getDecision(), Decision.ACCEPT);
        janVotesYes.setDecision(Decision.ABSTAIN);
        Assertions.assertEquals(janVotesYes.getDecision(), Decision.ABSTAIN);
    }

    @Test
    void setDecisionSame() {
        TypelessVote janVote = new TypelessVote("jan123");
        ProposalPk proposalPk = new ProposalPk("prop", new HoaId("hoa"));
        ProposalVote janVotesYes = new ProposalVote(janVote, proposalPk, Decision.ACCEPT);

        Assertions.assertEquals(janVotesYes.getDecision(), Decision.ACCEPT);
        janVotesYes.setDecision(Decision.ACCEPT);
        Assertions.assertEquals(janVotesYes.getDecision(), Decision.ACCEPT);
    }
}