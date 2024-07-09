package com.proriberaapp.ribera.Api.controllers.payme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TransactionResponse {
    private String action;
    private String id;
    private String success;
    private Transaction transaction;
    private Object validations;
    private Object token;

    @Data
    @NoArgsConstructor
    public static class Transaction {
        private String currency;
        private String amount;
        private Meta meta;
    }

    @Data
    @NoArgsConstructor
    public static class Meta {
        @JsonProperty("internal_operation_number")
        private String internalOperationNumber;
        private String description;
        private Processor processor;
        @JsonProperty("additional_fields")
        private Map<String, String> additionalFields;
        private Status status;
    }

    @Data
    @NoArgsConstructor
    public static class Processor {
        private Authorization authorization;
    }

    @Data
    @NoArgsConstructor
    public static class Authorization {
        private String code;
    }

    @Data
    @NoArgsConstructor
    public static class Status {
        private String code;
        @JsonProperty("message_ilgn")
        private List<Message> messageIlgn;
    }

    @Data
    @NoArgsConstructor
    public static class Message {
        private String locale;
        private String value;
    }
}