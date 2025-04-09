package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem.activity.domain.Organizer;
import nl.tudelft.sem.activity.domain.OrganizerAttributeConverter;
import org.junit.jupiter.api.Test;

public class OrganizerAttributeConverterTest {
    private final OrganizerAttributeConverter conv = new OrganizerAttributeConverter();

    @Test
    public void convertToDatabaseColumnTest() {
        Organizer vladi = new Organizer("vladi");
        String expected = "vladi";
        assertEquals(conv.convertToDatabaseColumn(vladi), expected);
    }

    @Test
    public void convertToEntityAttributeTest() {
        Organizer expected = new Organizer("vladi");
        String vladi = "vladi";
        Organizer converted = conv.convertToEntityAttribute(vladi);
        assertEquals(converted, expected);
    }
}
