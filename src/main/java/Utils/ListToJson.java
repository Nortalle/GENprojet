package Utils;

import com.google.gson.JsonObject;

public interface ListToJson<T> {
    JsonObject toJson(T t);
}
