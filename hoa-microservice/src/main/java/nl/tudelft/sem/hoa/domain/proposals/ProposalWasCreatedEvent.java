package nl.tudelft.sem.hoa.domain.proposals;

/**
 * A DDD domain event indicating a proposal was created.
 */
public class ProposalWasCreatedEvent {

    private final transient ProposalPk proposalPk;

    /**
     * Create new event for when proposal was created.
     *
     * @param proposalPk The proposal ID
     */
    public ProposalWasCreatedEvent(ProposalPk proposalPk) {
        this.proposalPk = proposalPk;
    }

    public ProposalPk getProposalId() {
        return proposalPk;
    }
}
