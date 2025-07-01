package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.OccupancyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OccupancyRepository extends R2dbcRepository<OccupancyEntity, Integer> {

    @Query(value = """
            SELECT
                COUNT(o.id) AS total_filtered_records
            FROM
                occupancy o
            WHERE
                (:searchTerm IS NULL)
                OR
                (
                    TRIM(UPPER(o.rule_name)) LIKE '%' || TRIM(UPPER(:searchTerm)) || '%'
                );
            """)
    Mono<Integer> countListOccupancyByOccupancyAndDays(String searchTerm);

    @Query(value = """
            SELECT *
            FROM occupancy o
            WHERE
                (:searchTerm IS NULL)
                OR
                (
                    TRIM(UPPER(o.rule_name)) LIKE '%' || TRIM(UPPER(:searchTerm)) || '%'
                )
            LIMIT :size OFFSET :offset;
            """)
    Flux<OccupancyEntity> getListByOccupancyAndDays(String searchTerm, Integer size, Integer offset);

    Mono<OccupancyEntity> findById(Integer id);
}
