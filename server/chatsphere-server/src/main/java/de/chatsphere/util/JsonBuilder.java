package de.chatsphere.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.Collections;
import java.util.Map;

/**
 * A convenient helper class to serialize/deserialize json objects.
 */
public class JsonBuilder {

  public static final Gson GSON = new GsonBuilder()
    /*
     * This is important because the graphql spec says that null values should be
     * present
     */
    .serializeNulls().create();

  /**
   * Converts a JSON-stringified message to a java map. Each JSON fields is mapped to an equivalent
   * String index in the Java map.
   *
   * @param jsonStr - a received message
   *
   * @return map - converted map
   */
  static Map<String, Object> toMap(String jsonStr) {
    if (jsonStr == null || jsonStr.trim().length() == 0) {
      return Collections.emptyMap();
    }
    // Gson uses name tokens for generic input like Map<String,Object>
    TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {
    };
    Map<String, Object> map = GSON.fromJson(jsonStr, typeToken.getType());
    return map == null ? Collections.emptyMap() : map;
  }

  /**
   * Converts a java object into a json object.
   *
   * @param obj the java object
   *
   * @return the json object
   */
  public static String toJsonString(Object obj) {
    return GSON.toJson(obj);
  }
}
