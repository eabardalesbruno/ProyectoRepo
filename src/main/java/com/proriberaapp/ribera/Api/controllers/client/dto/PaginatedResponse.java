package com.proriberaapp.ribera.Api.controllers.client.dto;
import java.util.List;

public class PaginatedResponse<T> {
    private long totalElements;
    private List<T> content;

    public PaginatedResponse(long totalElements, List<T> content) {
        this.totalElements = totalElements;
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}