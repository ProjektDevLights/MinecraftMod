package de.devlights.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ArraySerialiazer<T> implements JsonSerializer<T[]> {

    @Override
    public JsonElement serialize(T[] source, Type type, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (T item : source) {
            if (item != null) {
                jsonArray.add(context.serialize(item));
            }
        }
        return jsonArray;
    }
}
