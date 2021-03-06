package Utils;

import com.google.gson.*;

import java.util.ArrayList;

public class JsonUtility {
    private static final Gson GSON = new GsonBuilder().create();

    /**
     * @param json string to transform into Json
     * @return Json of the string
     */
    public static JsonElement fromJson(String json) {
        return GSON.fromJson(json, JsonElement.class);
    }

    /**
     * @param json Json to transform into string
     * @return string of Json
     */
    public static String toJson(JsonElement json) {
        return GSON.toJson(json);
    }

    /**
     *
     * @param ts the list of MyClass
     * @param lambda MyClass::toJson
     * @param <T> MyClass
     * @return a json array
     */
    public static <T> JsonArray listToJson(ArrayList<T> ts, ListToJson<T> lambda) {
        JsonArray list = new JsonArray();
        for(T t : ts) list.add(lambda.toJson(t));
        return list;
    }

    /**
     *
     * @param list the json array
     * @param lambda MyClass::new
     * @param <T> MyClass
     * @return ArrayList of MyClass
     */
    public static <T> ArrayList<T> listFromJson(JsonArray list, ListFromJson<T> lambda) {
        ArrayList<T> ts = new ArrayList<>();
        for(JsonElement j : list) ts.add(lambda.fromJson((JsonObject) j));
        return ts;
    }
}
