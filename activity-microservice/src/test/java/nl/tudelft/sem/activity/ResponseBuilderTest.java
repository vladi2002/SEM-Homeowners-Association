package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.Response;
import nl.tudelft.sem.activity.domain.ResponseBuilder;
import nl.tudelft.sem.activity.domain.ResponseOption;
import org.junit.jupiter.api.Test;

public class ResponseBuilderTest {
    @Test
    public void constructorTest() {
        ResponseBuilder builder = new ResponseBuilder();
        assertNotNull(builder);
    }

    @Test
    public void setResponseOptionTest() {
        ResponseBuilder builder = new ResponseBuilder();
        builder.setResponseOption(ResponseOption.GOING);
        assertEquals(builder.getOption(), ResponseOption.GOING);
    }

    @Test
    public void getResponseOptionTest() {
        ResponseBuilder builder = new ResponseBuilder();
        builder.setResponseOption(ResponseOption.GOING);
        builder.setResponseOption(ResponseOption.INTERESTED);
        assertEquals(builder.getOption(), ResponseOption.INTERESTED);
    }

    @Test
    public void getResponderNameTest() {
        ResponseBuilder builder = new ResponseBuilder();
        builder.setResponseOption(ResponseOption.GOING);
        builder.setResponderName("Bram");
        assertEquals(builder.getResponderName(), "Bram");
    }

    @Test
    public void setResponderNameTest() {
        ResponseBuilder builder = new ResponseBuilder();
        builder.setResponseOption(ResponseOption.GOING);
        builder.setResponderName("Bram");
        builder.setResponderName("Vladi");
        assertEquals(builder.getResponderName(), "Vladi");
    }

    @Test
    public void buildTest() {
        ResponseBuilder builder = new ResponseBuilder();
        builder.setResponseOption(ResponseOption.GOING);
        builder.setResponderName("Bram");
        builder.setResponderName("Vladi");
        Response res = builder.build();
        assertNotNull(res);
        assertEquals(res.getResponderName(), "Vladi");
        assertEquals(res.getResponse(), ResponseOption.GOING);
    }
}
