package nl.tudelft.sem.activity.domain;

public class ResponseBuildDirector {
    private final transient Builder builder;

    public ResponseBuildDirector(Builder builder) {
        this.builder = builder;
    }

    public void construct(ResponseOption res, String name) {
        this.builder.setResponseOption(res);
        this.builder.setResponderName(name);
    }

    public Response buildActivityResponse() {
        return this.builder.build();
    }
}
