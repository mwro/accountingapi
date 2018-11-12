package json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import static json.StatusResponse.*;

public class JsonUtil {
    public static String getJsonWithStatusAndData(Status status, JsonElement data) {
        return new Gson().toJson(new StatusResponse(status, data));
    }

    public static String getJsonWithStatusAndMessage(Status status, String message) {
        return new Gson().toJson(new StatusResponse(status, message));
    }
}
