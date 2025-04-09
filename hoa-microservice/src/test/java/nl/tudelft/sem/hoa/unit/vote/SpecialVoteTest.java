package nl.tudelft.sem.hoa.unit.vote;

import nl.tudelft.sem.hoa.domain.vote.ElectionVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpecialVoteTest {

    @Test
    void getUserIdNormal() {
        TypelessVote janVote = new TypelessVote("jan123");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");

        Assertions.assertEquals(janVotesForPiet.getUserId(), "jan123");
    }

    @Test
    void getUserIdEmpty() {
        TypelessVote janVote = new TypelessVote("");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");

        Assertions.assertEquals(janVotesForPiet.getUserId(), "");
    }

    @Test
    void getVote() {
        TypelessVote janVote = new TypelessVote("");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");

        Assertions.assertEquals(janVotesForPiet.getVote(), janVote);
    }

    @Test
    void testEquals() {
        TypelessVote janVote = new TypelessVote("");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");
        TypelessVote hackerVote = janVote;
        ElectionVote hackerVoteForPiet = new ElectionVote(janVote, "election1", "piet123");
        Object o = new String("Invalid");
        Assertions.assertTrue(janVote.equals(hackerVote) && hackerVote.equals(janVote));
        Assertions.assertTrue(janVotesForPiet.equals(hackerVoteForPiet) && hackerVoteForPiet.equals(janVotesForPiet));
        Assertions.assertFalse(janVote.equals(o));
    }

    @Test
    void testEquals2() {
        TypelessVote janVote = new TypelessVote("");
        TypelessVote secondVote = new TypelessVote("second");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");
        TypelessVote hackerVote = janVote;
        ElectionVote hackerVoteForPiet = new ElectionVote(secondVote, "election1", "piet123");
        ElectionVote hackerVoteForPiet2 = hackerVoteForPiet;
        Object o = new String("Invalid");
        Assertions.assertTrue(hackerVoteForPiet2.equals(hackerVoteForPiet) && hackerVoteForPiet.equals(hackerVoteForPiet2));
        Assertions.assertFalse(janVotesForPiet.equals(hackerVoteForPiet));
        Assertions.assertFalse(hackerVoteForPiet.equals(janVotesForPiet));
        Assertions.assertFalse(hackerVoteForPiet.equals(secondVote));
    }

    @Test
    void testHashCode() {
        TypelessVote janVote = new TypelessVote("");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");
        TypelessVote hackerVote = new TypelessVote("");
        ElectionVote hackerVoteForPiet = new ElectionVote(janVote, "election1", "piet123");

        Assertions.assertEquals(janVotesForPiet.hashCode(), hackerVoteForPiet.hashCode());
    }

    @Test
    void testHashCode2() {
        TypelessVote janVote = new TypelessVote("");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");
        TypelessVote secondVote = new TypelessVote("second");
        ElectionVote hackerVoteForPiet = new ElectionVote(secondVote, "election1", "piet123");

        Assertions.assertNotEquals(janVotesForPiet.hashCode(), hackerVoteForPiet.hashCode());
    }
}