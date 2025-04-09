package nl.tudelft.sem.hoa.domain.proposals.exceptions;

import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;

public class ProposalVotingIsClosedException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public ProposalVotingIsClosedException(ProposalPk proposalPk) {
        super(proposalPk.toString());
    }

    @Override
    public String getMessage() {
        return "Proposal is not opened (Too young or too old).";
    }
}
