package nl.tudelft.sem.hoa.domain.elections;

public class ElectionVotesWereUpdatedEvent {
    private final String electionId;

    private final String hoaId;

    public ElectionVotesWereUpdatedEvent(String electionId, String hoaId) {
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
