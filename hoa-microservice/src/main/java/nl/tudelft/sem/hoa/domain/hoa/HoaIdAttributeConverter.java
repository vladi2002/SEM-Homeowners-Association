package nl.tudelft.sem.hoa.domain.hoa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class HoaIdAttributeConverter implements AttributeConverter<HoaId, String> {

    @Override
    public String convertToDatabaseColumn(HoaId attribute) {
        return attribute.toString();
    }

    @Override
    public HoaId convertToEntityAttribute(String dbData) {
        return new HoaId(dbData);
    }
}
