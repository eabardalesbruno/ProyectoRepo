package com.proriberaapp.ribera.Api.controllers.payme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionNecessaryRequest {
    private Integer idUser;
    private Integer idBooking;
    private Integer idCard;
    private Currency currency; //PEN, USD
    private String holderCard;
    private String email;
    private EmitterCard emitterCard;
    private String numberCard;
    private String expirationDate;
    private String cvv;
    private String amount;
    private Boolean saveCard;


    @Getter
    public enum Currency {
        PEN("604"),
        USD("840");

        private final String code;

        Currency(String code) {
            this.code = code;
        }

    }

    @Getter
    public enum EmitterCard {
        VISA, MASTERCARD, AMERICAN_EXPRESS, DINERS_CLUB
    }
}
