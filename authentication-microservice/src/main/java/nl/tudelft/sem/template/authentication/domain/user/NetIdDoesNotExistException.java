package nl.tudelft.sem.template.authentication.domain.user;

/**
 * Exception to indicate that NetID does not exist.
 */
public class NetIdDoesNotExistException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public NetIdDoesNotExistException(NetId netId) {
        super(netId.toString());
    }
}
