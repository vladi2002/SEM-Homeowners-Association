package nl.tudelft.sem.activity.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the Organizer value object.
 */
@Converter
public class OrganizerAttributeConverter implements AttributeConverter<Organizer, String> {

    @Override
    public String convertToDatabaseColumn(Organizer attribute) {
        return attribute.toString();
    }

    @Override
    public Organizer convertToEntityAttribute(String dbData) {
        return new Organizer(dbData);
    }

}

