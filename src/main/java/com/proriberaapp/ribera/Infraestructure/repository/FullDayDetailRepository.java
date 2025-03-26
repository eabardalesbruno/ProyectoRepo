package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FullDayDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FullDayDetailRepository extends R2dbcRepository<FullDayDetailEntity,Integer> {

    Mono<Void> deleteByFulldayid(Integer fulldayid);
}
