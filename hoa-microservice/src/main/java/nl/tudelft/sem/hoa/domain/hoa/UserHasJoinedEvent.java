package nl.tudelft.sem.hoa.domain.hoa;

public class UserHasJoinedEvent {
    private final HoaId hoaId;
    private final String user;

    public UserHasJoinedEvent(HoaId hoaId, String user) {
        this.hoaId = hoaId;
        this.user = user;
    }

    public HoaId getHoaId() {
        return hoaId;
    }

    public String getUser() {
        return user;
    }
}
