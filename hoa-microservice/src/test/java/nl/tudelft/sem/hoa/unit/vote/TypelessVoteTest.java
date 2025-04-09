package nl.tudelft.sem.hoa.unit.vote;

import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TypelessVoteTest {

    @Test
    void getUserIdNormal() {
        TypelessVote janVote = new TypelessVote("jan123");
        Assertions.assertEquals(janVote.getUserId(), "jan123");
    }

    @Test
    void getUserIdEmpty() {
        TypelessVote hackerVote = new TypelessVote("");
        Assertions.assertEquals(hackerVote.getUserId(), "");
    }
}