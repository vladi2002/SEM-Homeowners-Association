package nl.tudelft.sem.hoa.unit.vote;

import nl.tudelft.sem.hoa.domain.vote.ElectionVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ElectionVoteTest {

    @Test
    void testGetElectionIdNormal() {
        TypelessVote janVote = new TypelessVote("jan123");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");

        Assertions.assertEquals(janVotesForPiet.getElectionId(), "election1");
    }

    @Test
    void testGetElectionIdEmpty() {
        TypelessVote janVote = new TypelessVote("jan123");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "", "piet123");

        Assertions.assertEquals(janVotesForPiet.getElectionId(), "");
    }

    @Test
    void testGetApplicantIdNormal() {
        TypelessVote janVote = new TypelessVote("jan123");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "piet123");

        Assertions.assertEquals(janVotesForPiet.getApplicantId(), "piet123");
    }

    @Test
    void testGetApplicantIdEmpty() {
        TypelessVote janVote = new TypelessVote("jan123");
        ElectionVote janVotesForPiet = new ElectionVote(janVote, "election1", "");

        Assertions.assertEquals(janVotesForPiet.getApplicantId(), "");
    }
}