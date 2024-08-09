package com.proriberaapp.ribera.Api.controllers.client.dto;

import java.util.Map;

public class CustomResponse {
    private String message;
    private Map<String, Integer> data;

    public CustomResponse(String message, Map<String, Integer> data) {
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
    }
}