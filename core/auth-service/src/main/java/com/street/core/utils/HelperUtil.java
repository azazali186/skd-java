package com.street.core.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class HelperUtil {

    @Autowired
    HttpServletRequest httpServletRequest;

    private static HelperUtil instance;

    private HelperUtil() {}

    public static HelperUtil getInstance() {
        if (instance == null) {
            instance = new HelperUtil();
        }
        return instance;
    }

    public String getUserIPAddress() {
        String ipAddress = httpServletRequest.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }

        return ipAddress;
    }

    public Map<String, Object> objectToMap(Object data) {
        ObjectMapper mapper = new ObjectMapper();

        // Create a module with a custom serializer for LocalDateTime
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        });

        // Register the module with the ObjectMapper
        mapper.registerModule(module);

        Map map = mapper.convertValue(data, Map.class);

        return map;
    }
}
