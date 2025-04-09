package nl.tudelft.sem.template.authentication.domain.user;

public class UserDoesNotExistException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public UserDoesNotExistException(NetId netId) {
        super(netId.toString());
    }
}
