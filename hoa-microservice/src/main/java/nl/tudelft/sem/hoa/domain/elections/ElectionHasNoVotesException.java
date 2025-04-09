package nl.tudelft.sem.hoa.domain.elections;

public class ElectionHasNoVotesException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    private String electionId;

    public ElectionHasNoVotesException(String electionId) {
        super(electionId);
    }
}
