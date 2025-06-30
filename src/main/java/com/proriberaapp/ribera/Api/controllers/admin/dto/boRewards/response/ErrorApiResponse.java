package com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorApiResponse {
    private boolean result;
    private String message;
    private Object data;
    private String timestamp;
    private int status;
}
