package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.OccupancyByRangeEntity;
import com.proriberaapp.ribera.Domain.entities.OccupancyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OccupancyByRangeRepository extends R2dbcRepository<OccupancyByRangeEntity, Integer> {

    @Query(value = """
            SELECT COUNT(obr.id) AS total_filtered_records
            FROM occupancy_by_ranges obr
            WHERE
                (
                    (:startDate IS NULL AND :endDate IS NULL)
                    OR
                    (
                        (:startDate IS NULL OR TO_DATE(obr.range_from_date, 'DD/MM/YYYY') >= TO_DATE(:startDate, 'DD/MM/YYYY'))
                        AND
                        (:endDate IS NULL OR TO_DATE(obr.range_to_date, 'DD/MM/YYYY') <= TO_DATE(:endDate, 'DD/MM/YYYY'))
                    )
                );
            """)
    Mono<Integer>countListOccupancyByRanges(String startDate, String endDate);

    @Query(value = """
            SELECT *
            FROM occupancy_by_ranges obr
            WHERE
                (
                    (:startDate IS NULL AND :endDate IS NULL)
                    OR
                    (
                        (:startDate IS NULL OR TO_DATE(obr.range_from_date, 'DD/MM/YYYY') >= TO_DATE(:startDate, 'DD/MM/YYYY'))
                        AND
                        (:endDate IS NULL OR TO_DATE(obr.range_to_date, 'DD/MM/YYYY') <= TO_DATE(:endDate, 'DD/MM/YYYY'))
                    )
                )
            ORDER BY obr.id
            LIMIT :size OFFSET :offset;
            """)
    Flux<OccupancyByRangeEntity>getListByRanges(String startDate, String endDate, Integer size, Integer offset);

    Mono<OccupancyByRangeEntity> findById(Integer id);
}
