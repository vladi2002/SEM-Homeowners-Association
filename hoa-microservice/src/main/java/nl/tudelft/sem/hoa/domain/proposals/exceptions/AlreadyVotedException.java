package nl.tudelft.sem.hoa.domain.proposals.exceptions;

import nl.tudelft.sem.hoa.domain.proposals.Proposal;

public class AlreadyVotedException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public AlreadyVotedException(Proposal proposal, String netId) {
        super(proposal.toString() + " " + netId);
    }

    @Override
    public String getMessage() {
        return "User has already voted on this proposal.";
    }
}
