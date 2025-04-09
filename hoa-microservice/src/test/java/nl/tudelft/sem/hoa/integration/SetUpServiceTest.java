package nl.tudelft.sem.hoa.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaAlreadyExistException;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaRepository;
import nl.tudelft.sem.hoa.domain.hoa.SetUpService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"mockSetUpServiceProfile"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SetUpServiceTest {

    @Autowired
    private transient HoaRepository hoaRepository;

    @Autowired
    private transient SetUpService setUpService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSetup1() throws Exception {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        when(setUpService.setUpHoa(any(), any(), any())).thenReturn(new Hoa(id, "SCHLAND", "Test"));
        when(setUpService.checkHoaIdIsUnique(id)).thenReturn(false);


        assertThat(setUpService.setUpHoa(id, "SCHLAND", "Test").getCountry()).isEqualTo("SCHLAND");

        assertThat(setUpService.setUpHoa(id, "SCHLAND", "Test").getCity()).isEqualTo("Test");

        assertThat(setUpService.setUpHoa(id, "SCHLAND", "Test")).isEqualTo(hoa);

        when(setUpService.setUpHoa(id, "SCHLAND", "Test")).thenThrow(new HoaAlreadyExistException(id));

        assertThrows(HoaAlreadyExistException.class, () -> setUpService.setUpHoa(id, "SCHLAND", "Test"));

    }

    @Test
    public void setUpHoaTest() throws Exception {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoa.setId(1);
        setUpService = new SetUpService(hoaRepository);
        assertEquals(hoa, setUpService.setUpHoa(id, "SCHLAND", "Test"));
        assertEquals(hoaRepository.findHoaByHoaId(id).get(), hoa);
    }

    @Test
    public void setUpHoaTestNull() throws Exception {
        HoaId id = null;
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoa.setId(1);
        setUpService = new SetUpService(hoaRepository);
        Exception e = assertThrows(NullPointerException.class, () -> setUpService.setUpHoa(id, "SCHLAND", "Test"));
        //assertEquals(e.getMessage(), null);
    }

    @Test
    public void setUpHoaTestAlreadyExists() throws Exception {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoa.setId(1);
        setUpService = new SetUpService(hoaRepository);
        setUpService.setUpHoa(id, "SCHLAND", "Test");
        Exception e = assertThrows(HoaAlreadyExistException.class, () -> setUpService.setUpHoa(id, "SCHLAND", "Test"));
        assertEquals(e.getMessage(), id.toString());
    }

    @Test
    public void checkHoaIdIsUniqueTest() throws Exception {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoa.setId(1);
        setUpService = new SetUpService(hoaRepository);
        assertTrue(setUpService.checkHoaIdIsUnique(id));
        setUpService.setUpHoa(id, "SCHLAND", "Test");
        assertFalse(setUpService.checkHoaIdIsUnique(id));
    }

    @Test
    public void findByCountryAndCityTest() throws Exception {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoa.setId(1);
        setUpService = new SetUpService(hoaRepository);
        setUpService.setUpHoa(id, "SCHLAND", "Test");
        Exception e = assertThrows(Exception.class, () -> setUpService.findByCountryAndCity(null, "Sofia"));
        assertEquals(e.getMessage(), null);
        Exception e1 = assertThrows(Exception.class, () -> setUpService.findByCountryAndCity("Netherlands", null));
        assertEquals(e1.getMessage(), null);
        assertEquals(hoa, setUpService.findByCountryAndCity("SCHLAND", "Test").get().get(0));
    }

    @Test
    public void findHoaByHoaIdTest() throws Exception {
        HoaId id = new HoaId("Test123");
        Hoa hoa = new Hoa(id, "SCHLAND", "Test");
        hoa.setId(1);
        setUpService = new SetUpService(hoaRepository);
        setUpService.setUpHoa(id, "SCHLAND", "Test");
        assertEquals(hoa, setUpService.findHoaByHoaId("Test123"));
        Exception e = assertThrows(Exception.class, () -> setUpService.findHoaByHoaId(null));
        assertEquals(e.getMessage(), null);
    }

}
