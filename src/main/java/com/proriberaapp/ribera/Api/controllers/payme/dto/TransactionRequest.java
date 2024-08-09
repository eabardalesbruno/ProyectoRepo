package com.proriberaapp.ribera.Api.controllers.payme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String action;
    private String channel;
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
    private Transaction transaction;
    private Address address;
    @JsonProperty("card_holder")
    private List<CardHolder> cardHolders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethod {
        private List<Token> token;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token {
        private String id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Transaction {
        private String currency;
        private String amount;
        private Meta meta;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        @JsonProperty("internal_operation_number")
        private String internalOperationNumber;
        private String description;
        @JsonProperty("additional_fields")
        private Map<String, String> additionalFields;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private Billing billing;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Billing {
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        private String email;
        private Phone phone;
        private Location location;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Phone {
        @JsonProperty("country_code")
        private String countryCode;
        private String subscriber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        @JsonProperty("line_1")
        private String line1;
        @JsonProperty("line_2")
        private String line2;
        private String city;
        private String state;
        private String country;
        @JsonProperty("zip_code")
        private String zipCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardHolder {
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        @JsonProperty("email_address")
        private String emailAddress;
        @JsonProperty("identity_document_country")
        private String identityDocumentCountry;
        @JsonProperty("identity_document_type")
        private String identityDocumentType;
        @JsonProperty("identity_document_identifier")
        private String identityDocumentIdentifier;
    }
}
