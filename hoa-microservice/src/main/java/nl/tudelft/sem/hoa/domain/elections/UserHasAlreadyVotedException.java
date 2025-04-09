package nl.tudelft.sem.hoa.domain.elections;

public class UserHasAlreadyVotedException extends Exception {
    static final long serialVersionUID = -3387546993124229948L;

    private String electionId;

    public UserHasAlreadyVotedException(String electionId)  {
        super(electionId);
    }
}
