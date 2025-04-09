package nl.tudelft.sem.activity.domain;

import java.util.Objects;

/**
 * A DDD value object representing a HOA in our domain.
 */
public class HoaId {
    private final transient String hoaIdValue;

    public HoaId(String hoaIdValue) {
        // validate NetID
        this.hoaIdValue = hoaIdValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof HoaId) {
            HoaId hoa = (HoaId) o;
            return this.hoaIdValue.equals(hoa.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hoaIdValue);
    }

    @Override
    public String toString() {
        return hoaIdValue;
    }
}