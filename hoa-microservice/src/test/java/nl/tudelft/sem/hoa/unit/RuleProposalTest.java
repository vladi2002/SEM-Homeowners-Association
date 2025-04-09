package nl.tudelft.sem.hoa.unit;

import static nl.tudelft.sem.hoa.domain.vote.Decision.ABSTAIN;
import static nl.tudelft.sem.hoa.domain.vote.Decision.REJECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Month;
import java.util.List;
import nl.tudelft.sem.hoa.domain.CreationDate;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;
import nl.tudelft.sem.hoa.domain.proposals.ProposalType;
import nl.tudelft.sem.hoa.domain.proposals.RuleProposal;
import nl.tudelft.sem.hoa.domain.vote.ProposalVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import org.junit.jupiter.api.Test;

public class RuleProposalTest {

    @Test
    public void constructorTest() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal1 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        assertNotNull(proposal1);
        RuleProposal proposal2 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), voters, 2);
        assertNotNull(proposal2);
        RuleProposal proposal3 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                voters, 2);
        assertNotNull(proposal3);
    }

    @Test
    public void equalsTestSuccess() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal1 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        RuleProposal proposal2 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        RuleProposal proposal3 = proposal1;
        assertEquals(proposal1, proposal2);
        assertEquals(proposal1, proposal3);
        Object o = new String("Invalid");
        assertNotEquals(proposal1, o);
        RuleProposal proposal4 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 3);
        assertNotEquals(proposal1, proposal4);
    }

    @Test
    public void getRuleId() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal4 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 3);
        assertEquals(3, proposal4.getRuleId());
    }

    @Test
    public void hashCodeTest() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal1 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        RuleProposal proposal2 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        assertEquals(proposal1.hashCode(), proposal2.hashCode());
    }

    @Test
    public void updateVote() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal1 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);

        TypelessVote vo1 = new TypelessVote("Vladi");
        TypelessVote vo2 = new TypelessVote("Alex");
        ProposalVote vote1 = new ProposalVote(vo1, proposalPk, ABSTAIN);
        ProposalVote vote2 = new ProposalVote(vo2, proposalPk, REJECT);
        proposal1.updateVote(vote1);
        proposal1.updateVote(vote2);

        assertEquals(2, proposal1.getAbstainVotes());
        assertEquals(3, proposal1.getRejectVotes());
    }

    @Test
    public void proposalEquals() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        assertEquals(proposal, proposal);
    }

    @Test
    public void proposalNotEqualsNull() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        assertNotEquals(proposal, null);
    }

    @Test
    public void proposalNotEqualsString() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        assertNotEquals(proposal, "proposal");
    }

    @Test
    public void getType() {
        HoaId tud = new HoaId("TU Delft");
        List<String> voters = List.of("Vladi", "Alex", "Roland", "Jelt", "Rafa", "Bram", "Sergey");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        RuleProposal proposal1 = new RuleProposal(proposalPk, "Don't merge", ProposalType.RULE,
                new CreationDate(23, Month.DECEMBER, 2022), 4, 2, 1,
                voters, 2);
        assertEquals(ProposalType.RULE, proposal1.getProposalType());
        assertEquals("Don't merge", proposal1.getProposalString());
    }

    @Test
    public void proposalPkEquals() {
        HoaId tud = new HoaId("TU Delft");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        assertEquals(proposalPk, proposalPk);
    }

    @Test
    public void proposalPkNotEqualsNull() {
        HoaId tud = new HoaId("TU Delft");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        assertNotEquals(proposalPk, null);
    }

    @Test
    public void proposalPkNotEqualsString() {
        HoaId tud = new HoaId("TU Delft");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        assertNotEquals(proposalPk, "proposalPk");
    }

    @Test
    public void proposalPkHashCode() {
        HoaId tud = new HoaId("TU Delft");
        ProposalPk proposalPk = new ProposalPk("ProposalMerge", tud);
        assertEquals(proposalPk.hashCode(), proposalPk.hashCode());
    }

}
