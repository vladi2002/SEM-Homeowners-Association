package nl.tudelft.sem.activity.domain;

import java.util.Objects;

/**
 * A DDD value object representing a Description in our domain.
 */
public class Description {
    private final transient String activityDescription;

    public Description(String activityDescription) {
        // validate NetID
        this.activityDescription = activityDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Description) {
            Description desc = (Description) o;
            return this.activityDescription.equals(desc.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.activityDescription);
    }

    @Override
    public String toString() {
        return activityDescription;
    }
}
