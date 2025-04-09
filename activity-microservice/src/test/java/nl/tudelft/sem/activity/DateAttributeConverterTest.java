package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import nl.tudelft.sem.activity.domain.DateAttributeConverter;
import org.junit.jupiter.api.Test;

public class DateAttributeConverterTest {
    private final DateAttributeConverter conv = new DateAttributeConverter();
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm";

    @Test
    public void convertToDatabaseColumnTest() {
        Date date = new Date(2020, 12, 20, 12, 15);
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_PATTERN, Locale.GERMAN);
        String expected = formatter.format(date);
        assertEquals(conv.convertToDatabaseColumn(date), expected);
    }

    @Test
    public void convertToEntityAttributeTest() {
        Date expected = new Date(2020, 12, 20, 12, 15);
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_PATTERN, Locale.GERMAN);
        String date = formatter.format(expected);
        assertEquals(conv.convertToEntityAttribute(date), expected);
    }

    @Test
    public void convertToEntityAttributeExceptionTest() {
        String date = "ckhgdxtu,xlhgh,c";
        Exception e = assertThrows(RuntimeException.class, () -> conv.convertToEntityAttribute(date));
        assertEquals(e.getMessage(), "Date incorrectly defined parameters.");
    }
}
