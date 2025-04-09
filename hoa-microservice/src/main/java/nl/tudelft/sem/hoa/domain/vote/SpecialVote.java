package nl.tudelft.sem.hoa.domain.vote;

import java.util.Objects;

public abstract class SpecialVote implements Vote {
    protected transient Vote vote;

    public SpecialVote(Vote vote) {
        this.vote = vote;
    }

    public String getUserId() {
        return this.getVote().getUserId();
    }

    public Vote getVote() {
        return this.vote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialVote)) {
            return false;
        }
        SpecialVote that = (SpecialVote) o;
        return getVote().equals(that.getVote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVote());
    }
}
