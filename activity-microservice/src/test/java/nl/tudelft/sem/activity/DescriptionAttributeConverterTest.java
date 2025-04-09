package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.activity.domain.Description;
import nl.tudelft.sem.activity.domain.DescriptionAttributeConverter;
import org.junit.jupiter.api.Test;

public class DescriptionAttributeConverterTest {
    private final DescriptionAttributeConverter conv = new DescriptionAttributeConverter();

    @Test
    public void convertToDatabaseColumnTest() {
        Description desc = new Description("Cooking");
        String expected = "Cooking";
        assertEquals(conv.convertToDatabaseColumn(desc), expected);
    }

    @Test
    public void convertToEntityAttributeTest() {
        Description expected = new Description("Cooking");
        String desc = "Cooking";
        assertEquals(conv.convertToEntityAttribute(desc), expected);
    }
}
