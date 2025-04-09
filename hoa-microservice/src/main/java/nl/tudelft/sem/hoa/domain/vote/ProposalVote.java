package nl.tudelft.sem.hoa.domain.vote;

import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;

public class ProposalVote extends SpecialVote {

    private transient ProposalPk proposalPk;
    private transient Decision decision;

    /**A ProposalVote is a vote for a proposal.
     *
     * @param vote vote component used in the decorator pattern
     * @param proposalPk Id to determine which proposal this is about
     * @param decision ENUM Type can be: ABSTAIN, REJECT, ACCEPT
     */
    public ProposalVote(Vote vote, ProposalPk proposalPk, Decision decision) {
        super(vote);
        this.proposalPk = proposalPk;
        this.decision = decision;
    }

    public ProposalPk getProposalId() {
        return proposalPk;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }
}
