package json;

import com.google.gson.JsonElement;

public class StatusResponse {

    public enum Status {
        SUCCESS,
        FAIL
    }

    private final Status status;
    private final JsonElement data;
    private final String message;

    StatusResponse(Status status, String message) {
        this.status = status;
        this.message = message;
        data = null;
    }

    StatusResponse(Status status, JsonElement data) {
        this.status = status;
        this.data = data.deepCopy();
        message = null;
    }
}

