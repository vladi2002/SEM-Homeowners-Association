package nl.tudelft.sem.activity.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the Organizer value object.
 */
@Converter
public class HoaAttributeConverter implements AttributeConverter<HoaId, String> {

    @Override
    public String convertToDatabaseColumn(HoaId attribute) {
        return attribute.toString();
    }

    @Override
    public HoaId convertToEntityAttribute(String dbData) {
        return new HoaId(dbData);
    }

}

