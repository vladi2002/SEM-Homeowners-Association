package nl.tudelft.sem.hoa.domain.proposals.exceptions;

public class InvalidRuleIdException extends Exception {

    static final long serialVersionUID = -3387516993124229948L;

    public InvalidRuleIdException(int ruleId) {
        super(String.valueOf(ruleId));
    }

    @Override
    public String getMessage() {
        return "Invalid rule ID.";
    }
}
