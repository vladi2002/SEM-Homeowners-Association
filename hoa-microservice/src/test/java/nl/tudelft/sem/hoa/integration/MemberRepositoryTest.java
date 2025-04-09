package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)

public class MemberRepositoryTest {

    @Autowired
    HoaRepository rep;

    @Test
    public void repTest() throws Exception {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        rep.save(hoa);

        HoaId id2 = new HoaId("Test1234");
        Hoa hoa2 = new Hoa(id2, "SCHLAND", "Test");
        rep.save(hoa2);

        assertThat(rep.existsByHoaId(id)).isEqualTo(true);
        assertThat(rep.existsByHoaId(id2)).isEqualTo(true);
    }
}
