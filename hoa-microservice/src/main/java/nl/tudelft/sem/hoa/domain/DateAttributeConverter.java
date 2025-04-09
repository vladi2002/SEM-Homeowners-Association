package nl.tudelft.sem.hoa.domain;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DateAttributeConverter implements AttributeConverter<CreationDate, String> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    @Override
    public String convertToDatabaseColumn(CreationDate attribute) {
        return attribute.toString();
    }

    @Override
    public CreationDate convertToEntityAttribute(String dbData) {
        //return new CreationDate(LocalDate.parse(dbData, formatter));
        String[] split = dbData.split("-");
        return new CreationDate(Integer.valueOf(split[0]), Month.valueOf(split[1]), Integer.valueOf(split[2]));
    }
}

