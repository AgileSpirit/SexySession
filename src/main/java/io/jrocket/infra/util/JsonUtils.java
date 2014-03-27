package io.jrocket.infra.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static final class JsonProperty {
        final String key;
        final String value;

        public JsonProperty(final String key, final String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static String set(String data, JsonProperty property) throws ApplicationException {
        // Check property
        if (property.key == null || property.value == null) {
            throw new ApplicationException();
        }
        // Initialize result
        String result;
        Map<String, Object> session;
        try {
            // Deserialize data object
            if (data == null) {
                session = new LinkedHashMap<>();
            } else {
                session = mapper.readValue(data, new TypeReference<Map<String, Object>>(){});
            }
            // Add property
            session.put(property.key, property.value);
            // Reserialize data object
            result = mapper.writeValueAsString(session);
        } catch (IOException e) {
            throw new ApplicationException();
        }
        return result;
    }

}
