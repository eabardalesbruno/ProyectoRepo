package com.proriberaapp.ribera.Infraestructure.exception;

public class QuoteAndOfferIsAlreadyRegisteredException extends RuntimeException {
    public QuoteAndOfferIsAlreadyRegisteredException(String roomOfferName,String quoteName,String dayName) {
        super("La oferta " + roomOfferName + " y la cotizacion " + quoteName + " ya estan registradas en el dia " + dayName);

    }
    
}
