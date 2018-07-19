package com.yibo.zuul.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String string, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(string, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json数组转List
     */
    public static <T> T toList(String string, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(string, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}