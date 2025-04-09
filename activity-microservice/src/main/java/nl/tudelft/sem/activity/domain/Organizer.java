package nl.tudelft.sem.activity.domain;

import java.util.Objects;

/**
 * A DDD value object representing an Organizer in our domain.
 */
public class Organizer {
    private final transient String usernameValue;

    public Organizer(String usernameValue) {
        // validate NetID
        this.usernameValue = usernameValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Organizer) {
            Organizer org = (Organizer) o;
            return this.usernameValue.equals(org.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.usernameValue);
    }

    @Override
    public String toString() {
        return usernameValue;
    }
}
