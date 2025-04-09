package nl.tudelft.sem.hoa.domain.elections;

public class ElectionWasCreatedEvent {
    private final String electionId;

    private final String hoaId;

    public ElectionWasCreatedEvent(String electionId, String hoaId) {
        this.electionId = electionId;
        this.hoaId = hoaId;
    }

    public String getElectionId() {
        return electionId;
    }

    public String getHoaId() {
        return hoaId;
    }
}
