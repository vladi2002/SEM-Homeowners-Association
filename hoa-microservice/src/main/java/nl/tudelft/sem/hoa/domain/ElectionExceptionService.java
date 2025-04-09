package nl.tudelft.sem.hoa.domain;

import nl.tudelft.sem.hoa.domain.elections.ApplicantDoesNotExistException;
import nl.tudelft.sem.hoa.domain.elections.CantVoteForThemselvesException;
import nl.tudelft.sem.hoa.domain.elections.ElectionDoesntExistException;
import nl.tudelft.sem.hoa.domain.elections.ElectionHasNoVotesException;
import nl.tudelft.sem.hoa.domain.elections.ElectionIdAlreadyExistsException;
import nl.tudelft.sem.hoa.domain.elections.UserHasAlreadyVotedException;
import nl.tudelft.sem.hoa.domain.hoa.HoaDoesNotExistException;

public class ElectionExceptionService {



    public static void electionIdAlreadyExists(String electionId) throws ElectionIdAlreadyExistsException {
        throw new ElectionIdAlreadyExistsException(electionId);

    }

    public static void hoaDoesNotExist(String hoaId) throws HoaDoesNotExistException {
        throw new HoaDoesNotExistException(hoaId);

    }

    public static void cantVoteForThemselves(String userId) throws CantVoteForThemselvesException {
        throw new CantVoteForThemselvesException(userId);

    }

    public static void electionDoesntExist(String electionId) throws ElectionDoesntExistException {
        throw new ElectionDoesntExistException(electionId);

    }

    public static void applicantDoesNotExist(String electionId) throws ApplicantDoesNotExistException {
        throw new ApplicantDoesNotExistException(electionId);

    }

    public static void userHasAlreadyVoted(String electionId) throws UserHasAlreadyVotedException {
        throw new UserHasAlreadyVotedException(electionId);

    }

    public static void electionHasNoVotes(String electionId) throws ElectionHasNoVotesException {
        throw new ElectionHasNoVotesException(electionId);

    }

}
