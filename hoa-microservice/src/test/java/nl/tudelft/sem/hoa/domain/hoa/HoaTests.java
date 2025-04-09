package nl.tudelft.sem.hoa.domain.hoa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class HoaTests {

    @Test
    public void addressTest() {
        Address address = new Address("spain", "valencia", "Calle Pizarro",
                25, "46004");
        Address address2 = new Address("spain", "valencia", "Calle Pizarro",
                25, "46004");

        assertNotNull(address);
        assertEquals(address, address2);
        assertEquals("spain", address.getCountry());
        assertEquals("valencia", address.getCity());
        assertEquals("Calle Pizarro", address.getStreet());
        assertEquals(25, address.getHouseNumber());
        assertEquals("46004", address.getPostalCode());
        assertEquals("Address{ country='" + address.getCountry()
                + ", city='" + address.getCity()
                + ", street='" + address.getStreet()
                + ", houseNumber=" + address.getHouseNumber()
                + ", postalCode='" + address.getPostalCode() + '}', address.toString());
    }

    @Test
    public void hoaTest() {
        Hoa hoa = new Hoa(new HoaId("hoa"), "spain", "valencia");
        Hoa hoa2 = new Hoa(new HoaId("hoa"), "spain", "valencia");

        assertNotNull(hoa);
        assertEquals(hoa, hoa2);
        assertEquals(hoa.getHoaId(), new HoaId("hoa"));
        assertEquals(hoa.getCountry(), "spain");
        assertEquals(hoa.getCity(), "valencia");
        assertEquals("Hoa {"
                + hoa.getId()
                + ", hoaId= "
                + hoa.getHoaId()
                + ", country= "
                + hoa.getCountry()
                + ", city= "
                + hoa.getCity()
                + '}', hoa.toString());
    }

    @Test
    public void membersTest() {
        MemberAppUser memberAppUser = new MemberAppUser("rafa",
                new Hoa(new HoaId("hoa"), "spain", "valencia"),
                new Address("spain", "valencia", "Calle Pizarro", 25, "46004"));

        MemberAppUser memberAppUser2 = new MemberAppUser("rafa",
                new Hoa(new HoaId("hoa"), "spain", "valencia"),
                new Address("spain", "valencia", "Calle Pizarro", 25, "46004"));

        assertNotNull(memberAppUser);
        assertEquals(memberAppUser.getNetId(), "rafa");
        assertEquals(memberAppUser.getHoaId(), new Hoa(new HoaId("hoa"), "spain", "valencia").getHoaId());
        assertEquals(memberAppUser.getBoardMember(), false);
        assertEquals(memberAppUser.getTime(), memberAppUser.getTime());
        assertEquals(memberAppUser.getRole(), "MEMBER");
    }

}
