package nl.tudelft.sem.hoa.unit;

import nl.tudelft.sem.hoa.domain.hoa.Address;
import nl.tudelft.sem.hoa.domain.hoa.Hoa;
import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.MemberAppUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberAppUserTest {

    @Test
    void testIsNotBoardMember() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");

        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);

        Assertions.assertFalse(bram.getBoardMember());
    }

    @Test
    void testIsBoardMember() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");

        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);

        bram.setBoardMember(true);

        Assertions.assertTrue(bram.getBoardMember());
    }

    @Test
    void testGetHoaId() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");

        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);

        Assertions.assertEquals(bram.getHoaId(), krotWijk);
    }

    @Test
    void testSetBoardMember() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");

        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);

        Assertions.assertFalse(bram.getBoardMember());
        bram.setBoardMember(true);
        Assertions.assertTrue(bram.getBoardMember());
        bram.setBoardMember(false);
        Assertions.assertFalse(bram.getBoardMember());
    }

    @Test
    void testGetNetId() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");

        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);

        Assertions.assertEquals(bram.getNetId(), "bramy");
    }

    @Test
    void testEqualsReflexivity() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");

        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);

        Assertions.assertEquals(bram, bram);
    }

    //    @Test
    //    void testEqualsFeatures() {
    //        HoaId krotWijk = new HoaId("krot123");
    //        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");
    //
    //        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");
    //
    //        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);
    //        MemberAppUser bramClone = new MemberAppUser("bramy", beilenWest, bramWoning);
    //        Assertions.assertEquals(bram, bramClone);
    //    }

    @Test
    void testNotEquals() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");
        Hoa otherHoa = new Hoa(new HoaId("kot123"), "Netherlands", "Beilen");
        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);
        MemberAppUser o = new MemberAppUser("pietahr", beilenWest, bramWoning);
        MemberAppUser none = null;
        Object invalid = new String("Invalid");
        MemberAppUser other = new MemberAppUser("bramy", otherHoa, bramWoning);
        MemberAppUser evilBram = new MemberAppUser("bramy", beilenWest, bramWoning);
        evilBram.setBoardMember(true);
        Assertions.assertTrue(!bram.equals(o) && !o.equals(bram)
                && !bram.equals(none) && !bram.equals(invalid)
                && !bram.equals(other));
    }

    @Test
    void testHashCodeReflexivity() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");

        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);
        Assertions.assertEquals(bram.hashCode(), bram.hashCode());
    }

    @Test
    void testToString() {
        HoaId krotWijk = new HoaId("krot123");
        Hoa beilenWest = new Hoa(krotWijk, "Netherlands", "Beilen");

        Address bramWoning = new Address("Netherlands", "Beilen", "daBabyCarStraat", 1, "0593GG");

        MemberAppUser bram = new MemberAppUser("bramy", beilenWest, bramWoning);

        Assertions.assertEquals(bram.toString(), "MemberAppUser bramy");
    }
}