package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FullDayRateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FullDayRateRepository extends R2dbcRepository<FullDayRateEntity,Integer> {

    //@Query("SELECT * FROM fullday_rate ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    Flux<FullDayRateEntity> findAllByOrderByCreatedAtDesc();

    @Query(
        """
        SELECT *
        FROM fullday_rate
        WHERE (:title IS NULL OR LOWER(title) LIKE LOWER('%' || :title || '%'))
          AND (:rateStatus IS NULL OR rate_status = :rateStatus)
          AND (:rateType IS NULL OR CAST(:rateType AS text) = ANY(rate_type))
        ORDER BY created_at DESC
        """
    )
    Flux<FullDayRateEntity> searchRates(String title, Boolean rateStatus, String rateType);

    @Query("SELECT COUNT(*) FROM fullday_rate")
    Mono<Long> countAll();
}
