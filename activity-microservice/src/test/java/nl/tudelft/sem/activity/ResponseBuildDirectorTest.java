package nl.tudelft.sem.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nl.tudelft.sem.activity.domain.ActivityResponse;
import nl.tudelft.sem.activity.domain.Builder;
import nl.tudelft.sem.activity.domain.Response;
import nl.tudelft.sem.activity.domain.ResponseBuildDirector;
import nl.tudelft.sem.activity.domain.ResponseBuilder;
import nl.tudelft.sem.activity.domain.ResponseOption;
import org.junit.jupiter.api.Test;

public class ResponseBuildDirectorTest {

    @Test
    public void constructorTest() {
        Builder builder = new ResponseBuilder();
        ResponseBuildDirector director = new ResponseBuildDirector(builder);
        assertNotNull(director);
    }

    @Test
    public void constructTest() {
        Builder builder = new ResponseBuilder();
        ResponseBuildDirector director = new ResponseBuildDirector(builder);
        ResponseOption res = ResponseOption.INTERESTED;
        String responderName = "Bram";
        director.construct(res, responderName);
        Response response = director.buildActivityResponse();
        assertEquals(response, new ActivityResponse(res, responderName));
    }
}
