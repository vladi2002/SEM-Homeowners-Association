package nl.tudelft.sem.activity.domain;

/**
 * Exception to indicate the NetID is already in use.
 */
public class ActivityNotExistsException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public ActivityNotExistsException(int id) {
        super("Activity: " + id + " does not exist.");
    }
}
