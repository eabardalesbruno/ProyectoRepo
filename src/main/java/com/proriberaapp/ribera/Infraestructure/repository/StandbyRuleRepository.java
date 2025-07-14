package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response.StandByRuleByUserDetailDto;
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

    /*
    @Query(value = """
            SELECT
                sr.id_standby_rule as idstandbyrule,
                rtt.id_reservation_time_type as idreservationtimetype,
                rtt.type_name as typename,
                rtt.standby_hours as standbyhours,
                vt.visibility_name as visibilityname,
                CASE
                    WHEN rtt.type_name ILIKE '%horas%' THEN
                        CAST(SUBSTRING(rtt.type_name FROM '^\\d+') AS INTEGER)
                    WHEN rtt.type_name ILIKE '%dias%' THEN
                        CAST(SUBSTRING(rtt.type_name FROM '^\\d+') AS INTEGER) * 24
                    ELSE NULL
                END AS parsedhoursfromtypename
            FROM
                standby_rule sr
            INNER JOIN
                reservation_time_type rtt ON rtt.id_reservation_time_type = sr.id_reservation_time_type
            INNER JOIN
                visibility_type vt ON vt.id_visibility_type = sr.id_visibility_type
            WHERE
                rtt.status = 1
                AND (
                    (:isUserInclub = TRUE AND (vt.visibility_name = 'Socios' OR vt.visibility_name = 'Ambos'))
                    OR
                    (:isUserInclub = FALSE AND (vt.visibility_name = 'Clientes preferentes' OR vt.visibility_name = 'Ambos'))
                )
                AND (
                    CASE
                        WHEN rtt.type_name ILIKE '%horas%' THEN CAST(SUBSTRING(rtt.type_name FROM '^\\d+') AS INTEGER)
                        WHEN rtt.type_name ILIKE '%dias%' THEN CAST(SUBSTRING(rtt.type_name FROM '^\\d+') AS INTEGER) * 24
                        ELSE NULL
                    END <= :hoursDiff
                )
            ORDER BY
                parsedhoursfromtypename DESC
            LIMIT 1;
            """)
    Mono<StandByRuleByUserDetailDto> findStandByRuleByUser(Double hoursDiff, Boolean isUserInclub);
     */

    @Query(value = """
            SELECT
                sr.id_standby_rule AS idstandbyrule,
                rtt.id_reservation_time_type AS idreservationtimetype,
                rtt.type_name AS typename,
                rtt.standby_hours AS standbyhours,
                vt.visibility_name AS visibilityname,
                CASE
                    WHEN rtt.type_name ILIKE '%horas%' THEN
                        CAST(SUBSTRING(rtt.type_name FROM '^\\d+') AS INTEGER)
                    WHEN rtt.type_name ILIKE '%dias%' THEN
                        CAST(SUBSTRING(rtt.type_name FROM '^\\d+') AS INTEGER) * 24
                    ELSE NULL
                END AS parsedhoursfromtypename
            FROM
                standby_rule sr
            INNER JOIN
                reservation_time_type rtt ON rtt.id_reservation_time_type = sr.id_reservation_time_type
            INNER JOIN
                visibility_type vt ON vt.id_visibility_type = sr.id_visibility_type
            WHERE
                rtt.status = 1
                AND (
                    (:isUserInclub = TRUE AND (vt.visibility_name = 'Socios' OR vt.visibility_name = 'Ambos'))
                    OR
                    (:isUserInclub = FALSE AND (vt.visibility_name = 'Clientes preferentes' OR vt.visibility_name = 'Ambos'))
                );
            """)
    Flux<StandByRuleByUserDetailDto>findAllApplicableRulesByVisibility(Boolean isUserInclub);
}
