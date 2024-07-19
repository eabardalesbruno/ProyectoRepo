package com.proriberaapp.ribera.Api.controllers.payme.dto;

import com.proriberaapp.ribera.Api.controllers.payme.entity.AuthorizationEntity;
import com.proriberaapp.ribera.Api.controllers.payme.entity.PayMeAuthorization;
import com.proriberaapp.ribera.Domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationResponse {

    private Integer idBooking;
    private String action;
    private String id;
    private boolean success;
    private Transaction transaction;

    public static PayMeAuthorization create(Integer idUser, Role role, AuthorizationResponse authorizationResponse) {
        PayMeAuthorization entity = new PayMeAuthorization();
        entity.setIdBooking(authorizationResponse.getIdBooking());
        entity.setIdUser(idUser);
        entity.setRole(role);
        entity.setAction(authorizationResponse.getAction());
        entity.setId(authorizationResponse.getId());
        entity.setSuccess(authorizationResponse.isSuccess());
        entity.setCurrency(authorizationResponse.getTransaction().getCurrency());
        entity.setAmount(authorizationResponse.getTransaction().getAmount());
        entity.setInternalOperationNumber(authorizationResponse.getTransaction().getMeta().getInternal_operation_number());
        entity.setDescription(authorizationResponse.getTransaction().getMeta().getDescription());
        entity.setProcessorAuthorizationCode(authorizationResponse.getTransaction().getMeta().getProcessor().getAuthorization().getCode());
        entity.setAdditionalFields(authorizationResponse.getTransaction().getMeta().getAdditional_fields().toString());
        entity.setStatusCode(authorizationResponse.getTransaction().getMeta().getStatus().getCode());
        //entity.setMessageI18n(authorizationResponse.getTransaction().getMeta().getStatus().getMessage_ilgn().toString());
        entity.setMessageI18n(authorizationResponse.getTransaction().getMeta().getStatus().getMessage_ilgn().get(0).getValue());
        return entity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Transaction {
        private String currency;
        private String amount;
        private Meta meta;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private String internal_operation_number;
        private String description;
        private Processor processor;
        private Map<String, String> additional_fields;
        private Status status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Processor {
        private Authorization authorization;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Authorization {
        private String code;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private String code;
        private List<MessageI18n> message_ilgn;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageI18n {
        private String locale;
        private String value;
    }
}
