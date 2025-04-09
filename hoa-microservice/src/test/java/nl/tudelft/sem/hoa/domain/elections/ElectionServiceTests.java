package nl.tudelft.sem.hoa.domain.elections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaDoesNotExistException;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import nl.tudelft.sem.hoa.domain.vote.ElectionVote;
import nl.tudelft.sem.hoa.domain.vote.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@SuppressWarnings({"PMD", "Checkstyle"})
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder", "mockTokenGenerator", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ElectionServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    ElectionRepository electionRepository;

    @Mock
    MembersRepository membersRepository;

    @Mock
    HoaRepository hoaRepository;

    private transient ElectionService electionService;

    private transient Vote vote;

    /**
     * Sets up the stuff before the tests.
     */
    @BeforeEach
    public void setUp() {
        vote = mock(Vote.class);
        when(vote.getUserId()).thenReturn("rafa");
        electionService = new ElectionService(electionRepository, hoaRepository, membersRepository);
    }

    @Test
    public void voteTestCantVote() throws Exception {


        //cannot vote for yourself
        ElectionVote electionVote = new ElectionVote(vote, "a", "rafa");
        assertThrows(CantVoteForThemselvesException.class, () -> {
            electionService.vote(electionVote, "rafa");
        });
    }

    @Test
    public void voteTestHoaDoesNotExist() throws Exception {


        //hoa does not exist
        HoaId hoaId = new HoaId("rafa");
        System.out.println("test1");
        when(hoaRepository.existsByHoaId(hoaId)).thenReturn(false);
        ElectionVote electionVote = new ElectionVote(vote, "a", "jorge");
        assertThrows(HoaDoesNotExistException.class, () -> {
            electionService.vote(electionVote, "rafa");
        });
    }

    @Test
    public void voteTestElectionDoesNotExist() throws Exception {
        //election does not exist
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        when(electionRepository.existsByElectionId("a")).thenReturn(false);
        ElectionVote electionVote = new ElectionVote(vote, "a", "jorge");
        assertThrows(ElectionDoesntExistException.class, () -> {
            electionService.vote(electionVote, "rafa");
        });
    }

    @Test
    public void voteTestApplicantNotApplicant() throws Exception {
        //applicant does not exist
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        when(electionRepository.existsByElectionId("a")).thenReturn(true);

        MemberAppUser jorge = new MemberAppUser("jorge",
                new Hoa(new HoaId("a"), "spain", "valencia"), new Address());

        when(membersRepository.findMemberAppUsersByUsername("jorge"))
                .thenReturn(Optional.of(jorge));

        ElectionVote electionVote = new ElectionVote(vote, "a", "jorge");
        assertThrows(ApplicantDoesNotExistException.class, () -> {
            electionService.vote(electionVote, "rafa");
        });
    }

    @Test
    public void voteTestApplicantNotInHoa() throws Exception {
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        when(electionRepository.existsByElectionId("a")).thenReturn(true);
        MemberAppUser jorge = new MemberAppUser("jorge",
                new Hoa(new HoaId("a"), "spain", "valencia"), new Address());

        jorge.setRole("APPLICANT");

        when(membersRepository.findMemberAppUsersByUsername("jorge"))
                .thenReturn(Optional.of(jorge));

        ElectionVote electionVote = new ElectionVote(vote, "a", "jorge");
        assertThrows(ApplicantDoesNotExistException.class, () -> {
            electionService.vote(electionVote, "rafa");
        });
    }

    @Test
    public void voteTestUserAlreadyVotes() throws Exception {
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        when(electionRepository.existsByElectionId("a")).thenReturn(true);

        MemberAppUser jorge = new MemberAppUser("jorge",
                new Hoa(new HoaId("rafa"), "spain", "valencia"), new Address());

        jorge.setRole("APPLICANT");

        System.out.println(jorge);

        when(membersRepository.findMemberAppUsersByUsername("jorge"))
                .thenReturn(Optional.of(jorge));

        ElectionVote electionVote = new ElectionVote(vote, "a", "jorge");

        Election election = new Election("a", "rafa");

        when(electionRepository.findByElectionId("a")).thenReturn(Optional.of(election));

        electionService.vote(electionVote, "rafa");

        assertThrows(UserHasAlreadyVotedException.class, () -> {
            electionService.vote(electionVote, "rafa");
        });
    }

    @Test
    public void voteTestFunctional() throws Exception {
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        when(electionRepository.existsByElectionId("a")).thenReturn(true);

        MemberAppUser jorge = new MemberAppUser("jorge",
                new Hoa(new HoaId("rafa"), "spain", "valencia"), new Address());

        jorge.setRole("APPLICANT");

        System.out.println(jorge);

        when(membersRepository.findMemberAppUsersByUsername("jorge"))
                .thenReturn(Optional.of(jorge));

        when(electionRepository.findByElectionId("a")).thenReturn(Optional.of(new Election("a", "rafa")));

        ElectionVote electionVote = new ElectionVote(vote, "a", "jorge");

        electionService.vote(electionVote, "rafa");

        Election election = new Election("a", "rafa");

        when(electionRepository.findByElectionId("a")).thenReturn(Optional.of(election));

        verify(electionRepository).save(election);

    }

    @Test
    public void createElection1() throws Exception {
        when(electionRepository.existsByElectionId("a")).thenReturn(true);
        assertThrows(ElectionIdAlreadyExistsException.class, () -> {
            electionService.createElection("a", "rafa");
        });
    }

    @Test
    public void createElection2() throws Exception {
        when(electionRepository.existsByElectionId("a")).thenReturn(false);
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(false);
        assertThrows(HoaDoesNotExistException.class, () -> {
            electionService.createElection("a", "rafa");
        });
    }

    @Test
    public void createElection3() throws Exception {
        when(electionRepository.existsByElectionId("a")).thenReturn(false);
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        electionService.createElection("a", "rafa");
        verify(electionRepository).save(new Election("a", "rafa"));
    }

    @Test
    public void electionResults1() throws Exception {
        when(electionRepository.existsByElectionId("a")).thenReturn(false);
        assertThrows(ElectionDoesntExistException.class, () -> {
            electionService.electionResult("a", "rafa");
        });
    }

    @Test
    public void electionResults2() throws Exception {
        when(electionRepository.existsByElectionId("a")).thenReturn(true);
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(false);
        when(electionRepository.findByElectionId("a")).thenReturn(Optional.of(new Election("a", "rafa")));
        assertThrows(HoaDoesNotExistException.class, () -> {
            electionService.electionResult("a", "rafa");
        });
    }

    @Test
    public void electionResults3() throws Exception {
        when(electionRepository.existsByElectionId("a")).thenReturn(true);
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        when(electionRepository.findByElectionId("a")).thenReturn(Optional.of(new Election("a", "rafa")));
        assertThrows(ElectionHasNoVotesException.class, () -> {
            electionService.electionResult("a", "rafa");
        });
    }

    @Test
    public void electionResults4() throws Exception {
        when(electionRepository.existsByElectionId("a")).thenReturn(true);
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        when(electionRepository.findByElectionId("a")).thenReturn(Optional.of(new Election("a", "alex")));
        assertThrows(HoaDoesNotExistException.class, () -> {
            electionService.electionResult("a", "alex");
        });
    }

    @Test
    public void electionResults5() throws Exception {
        when(electionRepository.existsByElectionId("a")).thenReturn(true);
        when(hoaRepository.existsByHoaId(new HoaId("rafa"))).thenReturn(true);
        HashMap<String, Integer> votes = new HashMap<>();
        votes.put("jorge", 1);
        when(electionRepository.findByElectionId("a")).thenReturn(Optional.of(new Election("a", "rafa", votes)));
        when(electionRepository.findByBoard(new HoaId("rafa"))).thenReturn(new ArrayList<MemberAppUser>());
        MemberAppUser jorge = new MemberAppUser("jorge", new Hoa(new HoaId("rafa"), "spain", "valencia"), new Address());
        when(membersRepository.findMemberAppUsersByUsername("jorge")).thenReturn(Optional.of(jorge));

        electionService.electionResult("a", "rafa");
        jorge.setBoardMember(true);
        
        verify(membersRepository).save(jorge);
        verify(electionRepository).delete(new Election("a", "rafa", votes));
    }
}
