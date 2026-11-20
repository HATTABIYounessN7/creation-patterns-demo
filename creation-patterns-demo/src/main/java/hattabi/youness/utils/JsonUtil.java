package hattabi.youness.utils;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> String toJson(T object) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }
}
