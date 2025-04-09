package nl.tudelft.sem.hoa.domain.elections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nl.tudelft.sem.hoa.domain.vote.ElectionVote;
import nl.tudelft.sem.hoa.domain.vote.TypelessVote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@SpringBootTest
@SuppressWarnings({"PMD", "Checkstyle"})
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder", "mockTokenGenerator", "mockAuthenticationManager"})
@AutoConfigureMockMvc
public class ElectionTests {

    @Test
    public void constructorGetter1() {
        Election election = new Election("id", "hoaId");
        assertNotNull(election);
        assertEquals("hoaId", election.getHoaId());
        assertEquals("id", election.getElectionId());
        assertEquals(new HashMap<>(), election.getVotes());
    }

    @Test
    public void constructorGetter2() {
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("user", 2);
        Election election = new Election("id", "hoaId", votes);
        assertNotNull(election);
        assertEquals("hoaId", election.getHoaId());
        assertEquals("id", election.getElectionId());
        assertEquals(votes.get("user"), election.getVotes().get("user"));
    }

    @Test
    public void updateVote1() {
        Election election = new Election("id", "hoaId");
        TypelessVote vo = new TypelessVote("user");
        ElectionVote vote = new ElectionVote(vo, "id", "applicant");
        election.updateVote(vote);
        assertEquals(1, election.getVotes().get("applicant"));
        assertEquals(true, election.userVoted("user"));
    }

    @Test
    public void updateVote2() {
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("user", 3);
        votes.put("applicant", 1);
        Election election = new Election("id", "hoaId", votes);
        TypelessVote vo = new TypelessVote("user");
        ElectionVote vote = new ElectionVote(vo, "id", "applicant");
        election.updateVote(vote);
        assertEquals(2, election.getVotes().get("applicant"));
        assertEquals(3, election.getVotes().get("user"));
        assertEquals(true, election.userVoted("user"));
    }

    @Test
    public void updateVotes1() {
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("user", 3);
        votes.put("applicant", 1);
        Election election = new Election("id", "hoaId", votes);
        TypelessVote vo = new TypelessVote("user");
        ElectionVote vote1 = new ElectionVote(vo, "id", "applicant");
        ElectionVote vote2 = new ElectionVote(vo, "id", "gamer");
        election.updateVotes(List.of(vote1, vote2));

        assertEquals(3, election.getVotes().get("user"));
        assertEquals(2, election.getVotes().get("applicant"));
        assertEquals(1, election.getVotes().get("gamer"));
        assertEquals(true, election.userVoted("user"));
    }


    @Test
    public void emptyResult() {
        Election election = new Election("id", "hoaId");
        assertNull(election.getResult());
    }

    @Test
    public void resultTest1() {
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("user", 1);
        Election election = new Election("id", "hoaId", votes);

        List<String> result = new ArrayList<>();
        result.add("user");

        assertEquals(result, election.getResult());
    }

    @Test
    public void resultTest2() {
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("user", 3);
        votes.put("applicant", 1);
        Election election = new Election("id", "hoaId", votes);

        List<String> result = new ArrayList<>();
        result.add("user");
        result.add("applicant");

        assertEquals(result, election.getResult());
    }

    @Test
    public void resultTest3() {
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("user", 1);
        votes.put("applicant", 3);
        Election election = new Election("id", "hoaId", votes);

        List<String> result = new ArrayList<>();
        result.add("user");
        result.add("applicant");

        assertEquals(result, election.getResult());
    }

    @Test
    public void resultTest4() {
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("user", 3);
        votes.put("applicant", 1);
        votes.put("gamer", 1);
        votes.put("student", 1);

        List<String> result = new ArrayList<>();
        result.add("student");
        result.add("gamer");
        result.add("user");

        Election election = new Election("id", "hoaId", votes);

        assertEquals(result, election.getResult());
    }


    @Test
    public void resultTest5() {
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("user", 3);
        votes.put("applicant", 2);
        votes.put("gamer", 3);
        votes.put("student", 1);

        List<String> result = new ArrayList<>();
        result.add("gamer");
        result.add("user");
        result.add("applicant");
        Election election = new Election("id", "hoaId", votes);

        assertEquals(result, election.getResult());
    }

    @Test
    public void equalsTest1() {
        Election election = new Election("id", "hoaId");
        Election election2 = new Election("id", "hoaId");
        assertEquals(election, election2);
    }

    @Test
    public void equalsTest2() {
        Election election = new Election("id", "hoaId");
        assertNotEquals("id", election);
    }

    @Test
    public void equalsTest3() {
        Election election = new Election("id", "hoaId");
        assertEquals(election, election);
    }

    @Test
    public void equalsTest4() {
        Election election = new Election("id", "hoaId");
        assertNotEquals(null, election);
    }

    @Test
    public void equalsTest5() {
        Election election = new Election("id", "hoaId");
        Election election2 = new Election("id", "hoaId");
        assertEquals(election.hashCode(), election2.hashCode());
    }

    @Test
    public void equalsTest6() {
        Election election = new Election("id", "hoaId");
        Election election2 = null;
        Object o = new String("Invalid");
        assertNotEquals(election, election2);
        assertNotEquals(election, o);
    }

}
