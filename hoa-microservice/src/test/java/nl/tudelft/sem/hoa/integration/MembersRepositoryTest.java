package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import nl.tudelft.sem.hoa.domain.hoa.MembersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MembersRepositoryTest {

    @Autowired
    MembersRepository members;

    @Test
    void existsByUsername() {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        MemberAppUser bramy = new MemberAppUser("bramy", hoa, new Address("SCHLAND", "Test", "Molenstraat", 1, "0123NJ"));

        members.save(bramy);

        assertThat(members.existsByUsername(bramy.getNetId())).isEqualTo(true);
        assertThat(members.existsByUsername("johan")).isEqualTo(false);
    }

    @Test
    void findMemberAppUsersByUsername() {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        MemberAppUser bramy = new MemberAppUser("bramy", hoa, new Address("SCHLAND", "Test", "null", 1, "null"));

        members.save(bramy);

        Assertions.assertEquals(members.findMemberAppUsersByUsername(bramy.getNetId()).orElse(null), bramy);
    }
}