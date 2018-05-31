package Utils;

import com.google.gson.JsonObject;

public interface ListFromJson<T> {
    T fromJson(JsonObject j);
}
