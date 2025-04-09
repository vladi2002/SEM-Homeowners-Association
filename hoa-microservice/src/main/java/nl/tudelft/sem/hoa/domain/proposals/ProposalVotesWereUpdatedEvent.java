package nl.tudelft.sem.hoa.domain.proposals;

import nl.tudelft.sem.hoa.domain.vote.Decision;

/**
 * A DDD domain event to indicate that a proposals votes were updated.
 */
public class ProposalVotesWereUpdatedEvent {

    private final transient ProposalPk proposalPk;
    private final transient Decision decision;

    public ProposalVotesWereUpdatedEvent(ProposalPk proposalPk, Decision decision) {
        this.proposalPk = proposalPk;
        this.decision = decision;
    }

    public ProposalPk getProposalId() {
        return proposalPk;
    }

    public Decision getDecision() {
        return decision;
    }
}
