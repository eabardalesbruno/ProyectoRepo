package com.proriberaapp.ribera.Domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorFieldResponse {
    private Integer code;
    private Map<String, String> fieldErrors;
}
