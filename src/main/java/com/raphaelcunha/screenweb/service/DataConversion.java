package com.raphaelcunha.screenweb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataConversion implements IDataConversion {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getData(String json, Class<T> myClass) {
        try {
            return mapper.readValue(json, myClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
