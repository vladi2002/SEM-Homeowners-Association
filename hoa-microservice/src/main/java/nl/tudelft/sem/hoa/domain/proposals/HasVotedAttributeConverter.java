package nl.tudelft.sem.hoa.domain.proposals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;

public class HasVotedAttributeConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        StringBuilder result = new StringBuilder();
        for (String s : attribute) {
            result.append(s);
            result.append(";");
        }
        if (result.length() == 0) {
            return null;
        }
        return result.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(dbData.split(";"));
    }
}
