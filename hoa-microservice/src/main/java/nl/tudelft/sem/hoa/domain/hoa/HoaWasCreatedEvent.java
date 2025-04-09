package nl.tudelft.sem.hoa.domain.hoa;


public class HoaWasCreatedEvent {
    private final HoaId hoaId;


    public HoaWasCreatedEvent(HoaId hoaId) {
        this.hoaId = hoaId;
    }

    public HoaId getHoaId() {
        return this.hoaId;
    }
}
