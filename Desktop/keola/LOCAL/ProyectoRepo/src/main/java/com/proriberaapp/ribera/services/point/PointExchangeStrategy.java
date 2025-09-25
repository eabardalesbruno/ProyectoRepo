package com.proriberaapp.ribera.services.point;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class PointExchangeStrategy implements PointsTransactionStrategy<PointExchangeDto> {

    @Override
    public Mono<PointExchangeDto> execute(PointExchangeDto request) {
        //Sacar el factor de los puntos (Ribera, Ribera top) del dia
        //Calcular el descuento agregando los puntos,
        return Mono.just(request);
    }

    
    
}
