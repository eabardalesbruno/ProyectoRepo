package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Data
@Builder
@Table("passwordresettoken")
public class PasswordResetTokenEntity {
    @Id
    @Column("userclientid")
    private Integer userClientId;

    @Column("token")
    private String token;

    @Column("passwordstate")
    private Integer passwordState;

    @Column("expirydate")
    private Timestamp expiryDate;

    // Constructor sin argumentos
    public PasswordResetTokenEntity() {}

    // Constructor con los argumentos requeridos
    public PasswordResetTokenEntity(Integer userClientId, String token, Integer passwordState, Timestamp expiryDate) {
        this.userClientId = userClientId;
        this.token = token;
        this.passwordState = passwordState;
        this.expiryDate = expiryDate;
    }
}