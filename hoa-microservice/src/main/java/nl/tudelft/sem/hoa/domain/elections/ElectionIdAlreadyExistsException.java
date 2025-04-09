package nl.tudelft.sem.hoa.domain.elections;

public class ElectionIdAlreadyExistsException extends Exception {
    static final long serialVersionUID = -3387546993124229948L;

    private String electionId;

    public ElectionIdAlreadyExistsException(String electionId)  {
        super(electionId);
    }
}
