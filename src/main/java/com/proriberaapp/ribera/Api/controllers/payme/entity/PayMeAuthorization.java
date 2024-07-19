package com.proriberaapp.ribera.Api.controllers.payme.entity;

import com.proriberaapp.ribera.Domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("pay_me_authorizations")
public class PayMeAuthorization {

    @Id
    @Column("idAuthorization")
    private Integer idAuthorization;

    @Column("idBooking")
    private Integer idBooking;

    @Column("idUser")
    private Integer idUser;

    @Column("role")
    private Role role; // Consider changing to an Enum if applicable

    @Column("action")
    private String action;

    @Column("id")
    private String id;

    @Column("success")
    private Boolean success;

    @Column("currency")
    private String currency;

    @Column("amount")
    private String amount;

    @Column("internal_operation_number")
    private String internalOperationNumber;

    @Column("description")
    private String description;

    @Column("processor_authorization_code")
    private String processorAuthorizationCode;

    @Column("additional_fields")
    private String additionalFields; // Consider using a Map or a custom class if you plan to deserialize

    @Column("status_code")
    private String statusCode;

    @Column("message_ilgn")
    private String messageI18n; // Consider using a List or a custom class if you plan to deserialize
}
