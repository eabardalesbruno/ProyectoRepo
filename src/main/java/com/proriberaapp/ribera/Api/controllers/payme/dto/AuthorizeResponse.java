package com.proriberaapp.ribera.Api.controllers.payme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorizeResponse {
    private String action;
    private boolean success;
    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("token_type")
    private String tokenType;
    private Authorization authorization;

    @Getter
    @Setter
    public static class Authorization {
        private Meta meta;
    }

    @Getter
    @Setter
    public static class Meta {
        private Status status;
    }

    @Getter
    @Setter
    public static class Status {
        private String code;
        @JsonProperty("message_ilgn")
        private List<Message> messageIlgn;
    }

    @Getter
    @Setter
    public static class Message {
        private String locale;
        private String value;
    }
}
