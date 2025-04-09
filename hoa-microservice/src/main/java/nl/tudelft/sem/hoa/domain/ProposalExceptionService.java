package nl.tudelft.sem.hoa.domain;

import nl.tudelft.sem.hoa.domain.hoa.HoaDoesNotExistException;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.proposals.Proposal;
import nl.tudelft.sem.hoa.domain.proposals.ProposalPk;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.AlreadyVotedException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.InvalidRuleIdException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.NotBoardMemberException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.NotPartOfHoaException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalIdAlreadyExistsException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalIdNotFoundException;
import nl.tudelft.sem.hoa.domain.proposals.exceptions.ProposalVotingIsClosedException;

public class ProposalExceptionService {

    public static void hoaIdNotFound(HoaId hoaId) throws HoaIdNotFoundException {
        throw new HoaIdNotFoundException(hoaId);

    }

    public static void proposalIdAlreadyExists(ProposalPk proposalPk) throws ProposalIdAlreadyExistsException {
        throw new ProposalIdAlreadyExistsException(proposalPk);

    }

    public static void invalidRuleId(int ruleId) throws InvalidRuleIdException {
        throw new InvalidRuleIdException(ruleId);

    }

    public static void ruleNotFound(int ruleId) throws RuleNotFoundException {
        throw new RuleNotFoundException(ruleId);

    }

    public static void alreadyVoted(Proposal proposal, String userId) throws AlreadyVotedException {
        throw new AlreadyVotedException(proposal, userId);

    }

    public static void  proposalVotingIsClosed(ProposalPk proposalPk) throws ProposalVotingIsClosedException {
        throw new ProposalVotingIsClosedException(proposalPk);

    }

    public static void  proposalIdNotFound(ProposalPk proposalPk) throws ProposalIdNotFoundException {
        throw new ProposalIdNotFoundException(proposalPk);

    }

    public static void  notPartOfHoa(String netId, HoaId hoaId) throws NotPartOfHoaException {
        throw new NotPartOfHoaException(netId, hoaId);

    }

    public static void  notBoardMember(MemberAppUser memberAppUser) throws NotBoardMemberException {
        throw new NotBoardMemberException(memberAppUser);

    }
}
