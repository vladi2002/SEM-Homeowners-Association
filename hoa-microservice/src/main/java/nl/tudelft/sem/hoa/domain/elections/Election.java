package nl.tudelft.sem.hoa.domain.elections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.hoa.domain.HasEvents;
import nl.tudelft.sem.hoa.domain.vote.ElectionVote;

@Entity
@Table(name = "elections")
@NoArgsConstructor
@SuppressWarnings("PMD")
public class Election extends HasEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", nullable = false, unique = true)
    private int id;

    @Column(name = "electionId", unique = true)
    private String electionId;

    @Column(name = "hoaId", nullable = false)
    private String hoaId;

    @ElementCollection
    @Column(name = "votes")
    private Map<String, Integer> votes;

    @ElementCollection
    @Column(name = "users")
    private Set<String> users;

    private final int numBoardMembers = 3;

    /**
     * constructor for election object.
     *
     * @param electionId description/date/name of the election
     * @param hoaId the id of the hoa in which the election takes place
     *              (doesn't check if the hoa exists yet)
     */
    public Election(String electionId, String hoaId) {
        this.electionId = electionId;
        this.hoaId = hoaId;
        this.votes = new HashMap<>();
        this.users = new HashSet<>();
        this.recordThat(new ElectionWasCreatedEvent(electionId, hoaId));
    }

    /**
     * constructor for election object.
     *
     * @param electionId description/date/name of the election
     * @param hoaId the id of the hoa in which the election takes place
     * @param votes a map of already existing votes that should be added to this election
     */
    public Election(String electionId, String hoaId, HashMap<String, Integer> votes) {
        this.electionId = electionId;
        this.hoaId = hoaId;
        this.votes = votes;
        this.users = new HashSet<>();
        this.recordThat(new ElectionWasCreatedEvent(electionId, hoaId));
    }

    public String getElectionId() {
        return electionId;
    }

    public String getHoaId() {
        return hoaId;
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    /**
     * Method to add a list of votes to the existing votes.
     *
     * @param votes list of votes to be added
     */
    public void updateVotes(List<ElectionVote> votes) {
        for (ElectionVote vote : votes) {
            updateVote(vote);
        }
    }

    /**
     * Method to add a new vote to the existing votes.
     *
     * @param vote the new vote to be added
     */
    public void updateVote(ElectionVote vote) {
        String appid = vote.getApplicantId();
        if (!votes.containsKey(appid)) {
            votes.put(appid, 1);
        } else {
            votes.put(appid, votes.get(appid) + 1);
        }
        users.add(vote.getUserId());
        this.recordThat(new ElectionVotesWereUpdatedEvent(electionId, hoaId));
    }

    public boolean userVoted(String applicationId) {
        return users.contains(applicationId);
    }

    /**
     * returns a list of the applicants with the most votes.
     *
     * @return list of applicants with the most votes
     */
    public List<String> getResult() {
        if (votes.isEmpty()) {
            return null;
        }

        List<String> board = new ArrayList<>();
        int lowest = -1;
        int pointer = -1;
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            if (board.size() < numBoardMembers) {
                board.add(entry.getKey());
                if (entry.getValue() < lowest || lowest == -1) {
                    lowest = entry.getValue();
                    pointer = board.size() - 1;
                }
            } else {
                if (entry.getValue() > lowest) {
                    board.remove(pointer);
                    board.add(entry.getKey());
                    lowest = entry.getValue();
                    pointer = board.size() - 1;
                }
            }
        }
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Election election = (Election) o;
        return electionId.equals(election.electionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electionId);
    }
}
