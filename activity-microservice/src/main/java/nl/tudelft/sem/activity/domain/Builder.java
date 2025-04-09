package nl.tudelft.sem.activity.domain;

public interface Builder {
    void setResponseOption(ResponseOption option);

    void setResponderName(String responderName);

    Response build();
}
