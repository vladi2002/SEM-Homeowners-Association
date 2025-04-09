package nl.tudelft.sem.activity.domain;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class ResponseBuilder implements Builder {
    private ResponseOption option;

    public ResponseOption getOption() {
        return this.option;
    }

    @Override
    public void setResponseOption(ResponseOption option) {
        this.option = option;
    }

    private String responderName;

    public String getResponderName() {
        return this.responderName;
    }

    @Override
    public void setResponderName(String responderName) {
        this.responderName = responderName;
    }

    @Override
    public Response build() {
        return new ActivityResponse(this.option, this.responderName);
    }
}
