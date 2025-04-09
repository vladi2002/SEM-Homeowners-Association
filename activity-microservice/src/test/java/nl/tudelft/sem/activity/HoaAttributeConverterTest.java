package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.activity.domain.HoaAttributeConverter;
import nl.tudelft.sem.activity.domain.HoaId;
import org.junit.jupiter.api.Test;

public class HoaAttributeConverterTest {
    private final HoaAttributeConverter conv = new HoaAttributeConverter();

    @Test
    public void convertToDatabaseColumnTest() {
        HoaId hoa = new HoaId("Delta Hill");
        String expected = "Delta Hill";
        assertEquals(conv.convertToDatabaseColumn(hoa), expected);
    }

    @Test
    public void convertToEntityAttributeTest() {
        HoaId expected = new HoaId("Delta Hill");
        String hoa = "Delta Hill";
        assertEquals(conv.convertToEntityAttribute(hoa), expected);
    }
}
