package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointQuotationDayDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;
import com.proriberaapp.ribera.Domain.entities.PointsTypeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PointsTypeRepository extends R2dbcRepository<PointsTypeEntity, Integer> {
    @Query("SELECT * FROM pointstype WHERE pointstypedesc LIKE CONCAT('%', :keyword, '%')")
    Flux<PointsTypeEntity> findByPointstypedescContaining(String keyword);

}
