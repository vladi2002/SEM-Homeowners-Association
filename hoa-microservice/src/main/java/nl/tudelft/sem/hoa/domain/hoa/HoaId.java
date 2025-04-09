package nl.tudelft.sem.hoa.domain.hoa;

import java.util.Objects;

public class HoaId {
    private final transient String hoaIdValue;

    public HoaId(String hoaId) {

        this.hoaIdValue = hoaId;
    }

    @Override
    public String toString() {
        return hoaIdValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HoaId hoaId = (HoaId) o;
        return Objects.equals(hoaIdValue, hoaId.hoaIdValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hoaIdValue);
    }
}
