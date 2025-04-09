package nl.tudelft.sem.hoa.domain.proposals.exceptions;

import javassist.NotFoundException;
import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;

public class ProposalIdNotFoundException extends NotFoundException {
    static final long serialVersionUID = -3387516993124229948L;

    public ProposalIdNotFoundException(ProposalPk proposalPk) {
        super(proposalPk.toString());
    }

    @Override
    public String getMessage() {
        return "Proposal ID not found.";
    }
}
