package com.proriberaapp.ribera.Api.controllers.payme.entity;

import com.proriberaapp.ribera.Api.controllers.payme.dto.AuthorizationResponse;
import com.proriberaapp.ribera.Domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("pay_me_authorizations")
public class AuthorizationEntity {
    @Id
    private Integer idAuthorization;
    private Integer idUser;
    private Role role;

    private String action;
    private String id;
    private boolean success;
    private Transaction transaction;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Transaction {
        private String currency;
        private String amount;
        private Meta meta;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        private String internal_operation_number;
        private String description;
        private Processor processor;
        private Map<String, String> additional_fields;
        private Status status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Processor {
        private Authorization authorization;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Authorization {
        private String code;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Status {
        private String code;
        private List<MessageI18n> message_ilgn;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageI18n {
        private String locale;
        private String value;
    }
}
