package nl.tudelft.sem.hoa.integration;

import nl.tudelft.sem.hoa.domain.JoinHoaService;
import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAlreadyInHoaException;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JoinHoaServiceTest {

    @Autowired
    MembersRepository membersRepository;

    @Test
    void joinHoaNewUser() throws Exception {
        Address beilenWest = new Address("SCHLAND", "Test", "null", 1, "null");
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        JoinHoaService joinHoaService = new JoinHoaService(membersRepository);
        MemberAppUser bramy = new MemberAppUser("bramy", hoa, beilenWest);

        Assertions.assertEquals(joinHoaService.joinHoa(hoa, "bramy", beilenWest).getNetId(), bramy.getNetId());
    }

    @Test
    void joinHoaAlreadyInHoa() {
        Address beilenWest = new Address("SCHLAND", "Test", "null", 1, "null");
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        JoinHoaService joinHoaService = new JoinHoaService(membersRepository);
        MemberAppUser bramy = new MemberAppUser("bramy", hoa, beilenWest);
        membersRepository.save(bramy);

        Assertions.assertThrows(MemberAlreadyInHoaException.class, () -> joinHoaService.joinHoa(hoa, "bramy", beilenWest));
    }

    @Test
    void checkUserIsInside() {
        Address beilenWest = new Address("SCHLAND", "Test", "null", 1, "null");
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        JoinHoaService joinHoaService = new JoinHoaService(membersRepository);
        MemberAppUser bramy = new MemberAppUser("bramy", hoa, beilenWest);
        membersRepository.save(bramy);

        Assertions.assertFalse(joinHoaService.checkUserIsInside("bramy"));
    }

    @Test
    void checkUserIsNotInside() {
        JoinHoaService joinHoaService = new JoinHoaService(membersRepository);
        Assertions.assertTrue(joinHoaService.checkUserIsInside("bramy"));
    }

    @Test
    void leaveHoaSuccesfull() throws Exception {
        Address beilenWest = new Address("SCHLAND", "Test", "null", 1, "null");
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        JoinHoaService joinHoaService = new JoinHoaService(membersRepository);
        MemberAppUser bramy = new MemberAppUser("bramy", hoa, beilenWest);
        membersRepository.save(bramy);

        Assertions.assertFalse(joinHoaService.checkUserIsInside("bramy")); //user is inside hoa
        joinHoaService.leaveHoa("bramy");
        Assertions.assertTrue(joinHoaService.checkUserIsInside("bramy")); //user is not inside hoa
    }

    @Test
    void leaveHoaNotSuccessful() {
        JoinHoaService joinHoaService = new JoinHoaService(membersRepository);
        Assertions.assertThrows(Exception.class, () -> joinHoaService.leaveHoa("bramy"));
    }

    @Test
    void findHoa() throws Exception {
        Address beilenWest = new Address("SCHLAND", "Test", "null", 1, "null");
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        JoinHoaService joinHoaService = new JoinHoaService(membersRepository);
        MemberAppUser bramy = new MemberAppUser("bramy", hoa, beilenWest);
        membersRepository.save(bramy);

        Assertions.assertEquals(joinHoaService.findHoa("bramy"), id);
    }

    @Test
    void findHoaFailure() {
        JoinHoaService joinHoaService = new JoinHoaService(membersRepository);
        Assertions.assertThrows(Exception.class, () -> joinHoaService.findHoa("bramy"));
    }
}