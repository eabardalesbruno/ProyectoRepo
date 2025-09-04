package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.ExchangeRateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRateEntity, Integer> {


    @Query("SELECT * FROM exchange_rate ORDER BY date DESC LIMIT 1")
    public Mono<ExchangeRateEntity> getCurrentExchangeRate();
}
