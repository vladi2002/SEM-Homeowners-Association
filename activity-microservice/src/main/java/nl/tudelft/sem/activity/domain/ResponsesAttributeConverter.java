package nl.tudelft.sem.activity.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the Organizer value object.
 */
@SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
@Converter
public class ResponsesAttributeConverter implements AttributeConverter<List<Response>, String> {

    @Override
    public String convertToDatabaseColumn(List<Response> attribute) {
        List<String> responses = new ArrayList<>();
        for (Response res : attribute) {
            responses.add(res.toString());
        }
        String encoded = "[";
        if (responses.size() == 0) {
            encoded += "]";
            System.out.println(encoded);
            return encoded;
        }
        if (responses.size() == 1) {
            encoded += responses.get(0).toString() + "]";
            System.out.println(encoded);
            return encoded;
        }
        for (int i = 0; i < responses.size() - 1; i++) {
            encoded += responses.get(i).toString() + ",";
        }
        encoded += responses.get(responses.size() - 1).toString() + "]";
        System.out.println(encoded);
        return encoded;
    }

    @Override
    public List<Response> convertToEntityAttribute(String dbData) {
        String data = dbData.replace("[", "").replace("]", "");
        List<String> responseStrings = Arrays.asList(data.split(",", -1));
        List<Response> responses = new ArrayList<>();
        for (String res : responseStrings) {
            String[] attributes = res.split(" ");
            responses.add(new ActivityResponse(ResponseOption.valueOf(attributes[0]), attributes[1]));
        }
        return responses;
    }

}

