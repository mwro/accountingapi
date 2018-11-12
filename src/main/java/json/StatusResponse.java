package json;

import com.google.gson.JsonElement;

public class StatusResponse {

    public enum Status {
        SUCCESS,
        FAIL
    }

    private Status status;
    private JsonElement data;
    private String message;

    public StatusResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public StatusResponse(Status status, JsonElement data) {
        this.status = status;
        this.data = data;
    }
}

