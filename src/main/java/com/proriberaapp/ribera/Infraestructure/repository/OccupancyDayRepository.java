package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.OccupancyDayEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OccupancyDayRepository extends R2dbcRepository<OccupancyDayEntity, Integer> {
    @Query(value = """
            SELECT *
            FROM occupancy_day od
            WHERE
                od.occupancy_id = :occupancyId
            """)
    Flux<OccupancyDayEntity> findByOccupancyId(Integer occupancyId);

    @Query(value = """
            UPDATE occupancy_day
            SET
                status = :newStatus,
                updated_at = NOW(),
                updated_by = :updatedBy
            WHERE
                occupancy_id = :occupancyId
                AND day_id = :dayId;
            """)
    Mono<Void>updateStatusByOccupancyIdAndDayId(Integer occupancyId,Integer dayId,Integer newStatus,String updatedBy);
}
