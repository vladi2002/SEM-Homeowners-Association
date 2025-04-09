package nl.tudelft.sem.activity.domain;

/**
 * A DDD domain event indicating a description had changed.
 */
public class DescriptionWasChangedEvent {
    private final Description description;

    public DescriptionWasChangedEvent(Description description) {
        this.description = description;
    }

    public Description getDescription() {
        return this.description;
    }
}
