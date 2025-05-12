package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalAuthResponse(
        boolean result,
        ExternalAuthData data
) {}

record ExternalAuthData(
        @JsonProperty("access_token") String accessToken
) {}
