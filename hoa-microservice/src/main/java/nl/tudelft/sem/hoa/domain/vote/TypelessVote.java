package nl.tudelft.sem.hoa.domain.vote;

import java.util.Objects;

public class TypelessVote implements Vote {
    protected transient String userId;

    /**A TypelessVote itself is useless. But it is needed to complete the decorator pattern.
     *
     * @param userId ID of the member that has voted this vote
     */
    public TypelessVote(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }
}
