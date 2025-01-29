package com.proriberaapp.ribera.Domain.entities;


import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@Table("password_reset_codes")
public class PasswordResetCodeEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("reset_code")
    private String reset_code;
    @Column("user_type")
    private String user_type;
    @Column("user_id")
    private Integer user_id;
     
    @Column("created_at")
    private LocalDate createdAt;
    @Column("expires_at")
    private LocalDateTime expiresAt;
    @Column("used")
    private Boolean used;
}
