package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Builder
@Table("passwordresettoken")
public class PasswordResetTokenEntity {
    @Id
    @Column("userid")
    private Integer userId;

    @Column("token")
    private String token;

    @Column("passwordstate")
    private Integer passwordstate;

    @Column("expirydate")
    private Timestamp expiryDate;

    // Constructor sin argumentos
    public PasswordResetTokenEntity() {}

    // Constructor con los argumentos requeridos
    public PasswordResetTokenEntity(Integer userid, String token, Integer passwordstate, Timestamp expiryDate) {
        this.userId = userid;
        this.token = token;
        this.passwordstate = passwordstate;
        this.expiryDate = expiryDate;
    }
}