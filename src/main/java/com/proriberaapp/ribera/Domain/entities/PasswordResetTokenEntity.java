package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Table("passwordresettoken")
public class PasswordResetTokenEntity {
    @Id
    private Long id;

    @Column("userid")
    private Long userid;

    @Column("token")
    private String token;

    @Column("expirydate")
    private Timestamp expiryDate;
}