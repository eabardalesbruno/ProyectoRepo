package com.proriberaapp.ribera.Api.controllers.payme.entity;

import com.proriberaapp.ribera.Domain.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("pay_me_tokenize")
public class TokenizeEntity {
    @Id
    private Integer idPayment;
    private Integer idUser;
    private Role role;

    private String token;
    private String bin;
    private String last_pan;
    private String brand;
    private String issuer;

    private Boolean active;
}
