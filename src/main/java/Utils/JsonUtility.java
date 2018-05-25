package Utils;

import com.google.gson.*;

public class JsonUtility {
    private static final Gson GSON = new GsonBuilder().create();

    public static JsonElement fromJson(String json) {
        return GSON.fromJson(json, JsonElement.class);
    }

    public static String toJson(JsonElement json) {
        return GSON.toJson(json);
    }
}
