package nl.tudelft.sem.hoa.domain;

import javassist.NotFoundException;

public class RuleNotFoundException extends NotFoundException {

    static final long serialVersionUID = -3387516993124229948L;

    public RuleNotFoundException(int ruleId) {
        super(String.valueOf(ruleId));
    }

    @Override
    public String getMessage() {
        return "Rule not found.";
    }
}
