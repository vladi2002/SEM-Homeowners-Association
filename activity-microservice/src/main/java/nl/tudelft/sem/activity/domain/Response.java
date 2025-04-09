package nl.tudelft.sem.activity.domain;

public interface Response {
    public ResponseOption getResponse();

    public String getResponderName();

    public void setResponse(ResponseOption response);

    @Override
    public boolean equals(Object o);

    @Override
    public String toString();

}
