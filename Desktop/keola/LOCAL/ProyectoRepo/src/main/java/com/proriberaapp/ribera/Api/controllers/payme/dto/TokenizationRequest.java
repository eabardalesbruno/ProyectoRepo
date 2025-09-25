package com.proriberaapp.ribera.Api.controllers.payme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenizationRequest {
    private String action;
    private List<Card> card;
    @JsonProperty("card_holder")
    private List<CardHolder> cardHolders;
    private Transaction transaction;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Card {
        private String pan;
        @JsonProperty("expiry_date")
        private String expiryDate;
        @JsonProperty("security_code")
        private String securityCode;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Transaction {
        private Meta meta;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        @JsonProperty("internal_operation_number")
        private String internalOperationNumber;
        @JsonProperty("additional_fields")
        private Map<String, String> additionalFields;
    }
}