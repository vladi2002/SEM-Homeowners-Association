package nl.tudelft.sem.hoa.domain.elections;

public class CantVoteForThemselvesException extends Exception {
    static final long serialVersionUID = -3387546993124229948L;

    private String userId;

    public CantVoteForThemselvesException(String userId)  {
        super(userId);
    }
}
