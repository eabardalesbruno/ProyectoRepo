package com.proriberaapp.ribera.Api.controllers.payme.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("pay_me")
public class PaymentEntity {
    @Id
    private Integer idPayment;
    private Integer idUser;

    private String token;
    private String bin;
    private String last_pan;
    private String brand;
    private String issuer;

    private Boolean active;
}
