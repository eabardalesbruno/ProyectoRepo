package com.proriberaapp.ribera.Api.controllers.payme.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenizationResponse {
    private String action;
    private Boolean success;
    private Transaction transaction;
    private List<Token> token;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Transaction {
        private Meta meta;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        @JsonProperty("commerce_id")
        private String commerceId;
        @JsonProperty("internal_operation_number")
        private String internalOperationNumber;
        private Status status;
        @JsonProperty("additional_fields")
        private Map<String, String> additionalFields;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private String code;
        @JsonProperty("message_ilgn")
        private List<Message> messageIlgn;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String locale;
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token {
        private String id;
        private Card card;
        @JsonProperty("creation_date")
        private String creationDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Card {
        private String bin;
        @JsonProperty("last_pan")
        private String lastPan;
        private String brand;
        private String issuer;
    }
}
