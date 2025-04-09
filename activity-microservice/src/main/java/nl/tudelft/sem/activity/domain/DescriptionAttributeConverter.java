package nl.tudelft.sem.activity.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the Organizer value object.
 */
@Converter
public class DescriptionAttributeConverter implements AttributeConverter<Description, String> {

    @Override
    public String convertToDatabaseColumn(Description attribute) {
        return attribute.toString();
    }

    @Override
    public Description convertToEntityAttribute(String dbData) {
        return new Description(dbData);
    }

}

