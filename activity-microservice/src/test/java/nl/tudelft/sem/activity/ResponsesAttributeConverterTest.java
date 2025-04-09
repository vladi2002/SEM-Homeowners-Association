package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.activity.domain.Builder;
import nl.tudelft.sem.activity.domain.Response;
import nl.tudelft.sem.activity.domain.ResponseBuilder;
import nl.tudelft.sem.activity.domain.ResponseOption;
import nl.tudelft.sem.activity.domain.ResponsesAttributeConverter;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

public class ResponsesAttributeConverterTest {
    private final ResponsesAttributeConverter conv = new ResponsesAttributeConverter();

    @Test
    public void convertToDatabaseColumnTest() {
        List<Response> responses = new ArrayList<>();
        Builder builder = new ResponseBuilder();
        builder.setResponderName("vladi");
        builder.setResponseOption(ResponseOption.GOING);
        Response responseActivity = builder.build();
        responses.add(responseActivity);
        builder.setResponderName("rafa");
        builder.setResponseOption(ResponseOption.NOT_INTERESTED);
        Response responseActivity2 = builder.build();
        responses.add(responseActivity2);
        builder.setResponderName("alex");
        builder.setResponseOption(ResponseOption.INTERESTED);
        Response responseActivity3 = builder.build();
        responses.add(responseActivity3);
        String expected = "[GOING vladi,NOT_INTERESTED rafa,INTERESTED alex]";
        assertEquals(conv.convertToDatabaseColumn(responses), expected);
    }

    @Test
    public void convertToDatabaseColumnTest0() {
        List<Response> responses = new ArrayList<>();
        String expected = "[]";
        assertEquals(conv.convertToDatabaseColumn(responses), expected);
    }

    @Test
    public void convertToDatabaseColumnTest1() {
        List<Response> responses = new ArrayList<>();
        Builder builder = new ResponseBuilder();
        builder.setResponderName("vladi");
        builder.setResponseOption(ResponseOption.GOING);
        Response responseActivity = builder.build();
        responses.add(responseActivity);
        String expected = "[GOING vladi]";
        assertEquals(conv.convertToDatabaseColumn(responses), expected);
    }

    @Test
    public void convertToEntityAttributeTest() {
        List<Response> responses = new ArrayList<>();
        Builder builder = new ResponseBuilder();
        builder.setResponderName("vladi");
        builder.setResponseOption(ResponseOption.GOING);
        Response responseActivity = builder.build();
        responses.add(responseActivity);
        builder.setResponderName("rafa");
        builder.setResponseOption(ResponseOption.NOT_INTERESTED);
        Response responseActivity2 = builder.build();
        responses.add(responseActivity2);
        builder.setResponderName("alex");
        builder.setResponseOption(ResponseOption.INTERESTED);
        Response responseActivity3 = builder.build();
        responses.add(responseActivity3);
        String concat = "[GOING vladi,NOT_INTERESTED rafa,INTERESTED alex]";
        assertEquals(conv.convertToEntityAttribute(concat), responses);
    }
}
