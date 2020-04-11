package com.tianyuan.easyim.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tianyuan.easyim.common.exception.JsonException;

public class JsonUtil {

    private static ObjectMapper ob = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return ob.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonException("toJson fail", e);
        }
    }

    public static <T> T fromJson(String jsonStr, Class<T> expectType) {
        try {
            return ob.readValue(jsonStr, expectType);
        } catch (JsonProcessingException e) {
            throw new JsonException("fromJson fail", e);
        }
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> typeReference) {
        try {
            return ob.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            throw new JsonException("fromJson fail", e);
        }
    }

    public static <T> T convert(Object obj, TypeReference<T> typeReference) {
        try {
            return ob.convertValue(obj, typeReference);
        } catch (IllegalArgumentException e) {
            throw new JsonException("convert fail", e);
        }
    }
}
