package com.raphaelcunha.screenweb.service;

public interface IDataConversion {
    <T> T getData(String json, Class<T> myClass);
}
