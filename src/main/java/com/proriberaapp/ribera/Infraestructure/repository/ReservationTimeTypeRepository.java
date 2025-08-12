package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.occupancyConfiguration.standbyRules.response.DropDownReservationTimeTypeResponse;
import com.proriberaapp.ribera.Domain.entities.ReservationTimeTypeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservationTimeTypeRepository extends R2dbcRepository<ReservationTimeTypeEntity,Integer> {
    @Query(value = """
            SELECT
                rtt.id_reservation_time_type AS idreservationtimetype,
                rtt.type_name AS reservationtimetypename,
                rtt.standby_hours AS standbyhours
            FROM reservation_time_type rtt
            WHERE
                rtt.status = 1
                AND (
                    (:searchTerm IS NULL)
                    OR (
                        TRIM(UPPER(rtt.type_name)) LIKE '%' || TRIM(UPPER(:searchTerm)) || '%'
                    )
                )
            ORDER BY rtt.id_reservation_time_type ASC;
            """)
    Flux<DropDownReservationTimeTypeResponse>getDropDownReservationTime(String searchTerm);

    @Query(value = """
            SELECT
                EXTRACT(EPOCH FROM (b.daybookinginit - b.createdat)) / 3600 AS hours_diff
            FROM
                booking AS b
            WHERE
                b.bookingid = :bookingId;
            """)
    Mono<Double> getHoursDiffByBookingId(Integer bookingId);

    @Query("SELECT * FROM reservation_time_type WHERE status = 1")
    Flux<ReservationTimeTypeEntity> findAllActive();

    @Query(value = """
            SELECT TO_CHAR(b.createdat, 'YYYY-MM-DD HH24:MI:SS.MS') AS creationtimestamp
            FROM booking b WHERE b.bookingId = :bookingId;
            """)
    Mono<String> getCreationTimestampByBookingId(Integer bookingId);
}
