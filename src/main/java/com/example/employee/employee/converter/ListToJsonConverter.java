package com.example.employee.employee.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class ListToJsonConverter implements AttributeConverter<List<Long>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (attribute == null) return "[]";
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting list to JSON string", e);
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return new ArrayList<>();
        try {
            return objectMapper.readValue(
                dbData,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class)
            );
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON string to list", e);
        }
    }
}
