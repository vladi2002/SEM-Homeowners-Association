package nl.tudelft.sem.activity.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the Organizer value object.
 */
@Converter
public class DateAttributeConverter implements AttributeConverter<Date, String> {
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm";

    @Override
    public String convertToDatabaseColumn(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_PATTERN, Locale.GERMAN);
        String dateLocal = formatter.format(date);
        return dateLocal;
    }

    @Override
    public Date convertToEntityAttribute(String dbData) {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_PATTERN, Locale.GERMAN);
        try {
            Date date = formatter.parse(dbData);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Date incorrectly defined parameters.", e);
        }
    }

}

