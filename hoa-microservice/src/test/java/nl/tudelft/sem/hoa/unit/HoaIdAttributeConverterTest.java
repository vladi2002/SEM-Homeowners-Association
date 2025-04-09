package nl.tudelft.sem.hoa.unit;

import nl.tudelft.sem.hoa.domain.hoa.HoaId;
import nl.tudelft.sem.hoa.domain.hoa.HoaIdAttributeConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HoaIdAttributeConverterTest {

    @Test
    void convertToDatabaseColumn() {
        HoaIdAttributeConverter toBeTested = new HoaIdAttributeConverter();
        Assertions.assertEquals(toBeTested.convertToDatabaseColumn(new HoaId("brak123")), "brak123");
    }

    @Test
    void convertToEntityAttribute() {
        HoaIdAttributeConverter toBeTested = new HoaIdAttributeConverter();
        Assertions.assertEquals(toBeTested.convertToEntityAttribute("brak123"), new HoaId("brak123"));
    }
}