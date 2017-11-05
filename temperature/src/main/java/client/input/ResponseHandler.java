package client.input;

public abstract class ResponseHandler<T> {

    private String responseBody;

    public abstract T getParsedOutput();

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
