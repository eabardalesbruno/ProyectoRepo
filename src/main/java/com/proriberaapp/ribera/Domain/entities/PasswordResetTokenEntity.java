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

@Getter
@Setter
@Builder
@Table("passwordresettoken")
public class PasswordResetTokenEntity {
    @Id
    @Column("userid")
    private Integer userid;

    @Column("token")
    private String token;

    @Column("expirydate")
    private Timestamp expiryDate;

    // Constructor with three arguments
    public PasswordResetTokenEntity(Integer userid, String token, Timestamp expiryDate) {
        this.userid = userid;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public PasswordResetTokenEntity() { userid = this.getUserid(); token=this.getToken();expiryDate=this.getExpiryDate();
    }
}