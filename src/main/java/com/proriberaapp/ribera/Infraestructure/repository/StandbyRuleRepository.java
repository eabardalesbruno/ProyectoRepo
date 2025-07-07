package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response.StandByRuleDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response.StandByRuleDto;
import com.proriberaapp.ribera.Domain.entities.StandbyRuleEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StandbyRuleRepository extends R2dbcRepository<StandbyRuleEntity, Integer> {

    @Query(value = """
            SELECT
                COUNT(sr.id_standby_rule) AS total_filtered_records
            FROM standby_rule sr
            INNER JOIN reservation_time_type rtt
                ON rtt.id_reservation_time_type = sr.id_reservation_time_type
            WHERE
                (:searchTerm IS NULL)
                OR (
                    TRIM(UPPER(rtt.type_name)) LIKE '%' || TRIM(UPPER(:searchTerm)) || '%'
                );
            """)
    Mono<Integer> countListStandByRules(String searchTerm);

    @Query(value = """
            SELECT
                sr.id_standby_rule AS idstandbyrule,
                rtt.type_name AS reservationtimetypename,
                rtt.standby_hours AS standbyhours
            FROM standby_rule sr
            INNER JOIN reservation_time_type rtt
                ON rtt.id_reservation_time_type = sr.id_reservation_time_type
            WHERE
                (:searchTerm IS NULL)
                OR (
                    TRIM(UPPER(rtt.type_name)) LIKE '%' || TRIM(UPPER(:searchTerm)) || '%'
                )
            ORDER BY sr.id_standby_rule
            LIMIT :size
            OFFSET :offset;
            """)
    Flux<StandByRuleDto> getListStandByRules(String searchTerm, Integer size, Integer offset);

    @Query(value = """
            SELECT
                sr.id_standby_rule AS idstandbyrule,
                sr.id_reservation_time_type AS idreservationtimetype,
                rtt.standby_hours AS standbyhours,
                sr.id_visibility_type AS idvisibilitytype
            FROM standby_rule sr
            INNER JOIN reservation_time_type rtt
                ON rtt.id_reservation_time_type = sr.id_reservation_time_type
            WHERE sr.id_standby_rule = :id;
            """)
    Mono<StandByRuleDetailDto> getStandByRuleDetail(Integer id);
}
