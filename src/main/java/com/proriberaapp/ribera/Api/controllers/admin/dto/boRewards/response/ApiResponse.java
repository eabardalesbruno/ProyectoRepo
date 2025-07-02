package com.proriberaapp.ribera.Api.controllers.admin.dto.boRewards.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean result;
    private T data;
    private Long  timestamp;
    private int status;
}
