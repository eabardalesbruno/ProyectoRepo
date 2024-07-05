package com.proriberaapp.ribera.Api.controllers.payme.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("payment")
public class PaymentEntity {
    @Id
    private Integer idPayment;
    private Integer idUser;

    private String token;
    private String cardHolderName;
    private String expirationDate;
    private Boolean active;
    private Boolean expired;

    public PaymentEntity(Object o, Integer idUser, Mono<TokenizationResponse> userId, String numberCard, String holderCard, String expirationDate, boolean b, boolean b1) {
    }
}
