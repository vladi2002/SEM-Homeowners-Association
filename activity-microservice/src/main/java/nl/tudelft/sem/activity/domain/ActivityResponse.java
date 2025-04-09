package nl.tudelft.sem.activity.domain;

import java.util.Objects;

public class ActivityResponse implements Response {
    private ResponseOption response;
    private final String responderName;

    public ActivityResponse(ResponseOption response, String responderName) {
        this.response = response;
        this.responderName = responderName;
    }

    @Override
    public ResponseOption getResponse() {
        return response;
    }

    @Override
    public String getResponderName() {
        return responderName;
    }

    @Override
    public void setResponse(ResponseOption response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ActivityResponse) {
            Response r = (ActivityResponse) o;
            return this.getResponderName().equals(r.getResponderName()) && this.getResponse().equals(r.getResponse());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(response, responderName);
    }

    @Override
    public String toString() {
        return getResponse().toString() + " " + getResponderName();
    }
}
