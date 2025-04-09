package nl.tudelft.sem.activity.domain;

/**
 * Exception to indicate the NetID is already in use.
 */
public class ActivityAlreadyCreatedException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public ActivityAlreadyCreatedException(Organizer organizer, Description description) {
        super("Organizer: " + organizer.toString() + " with description: " + description.toString() + " already exists.");
    }
}
