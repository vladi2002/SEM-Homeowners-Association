package nl.tudelft.sem.hoa.domain.proposals.exceptions;

import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;

public class ProposalIdAlreadyExistsException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public ProposalIdAlreadyExistsException(ProposalPk proposalPk) {
        super(proposalPk.toString());
    }

    @Override
    public String getMessage() {
        return "Proposal ID already exists.";
    }
}
