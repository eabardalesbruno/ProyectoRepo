package com.proriberaapp.ribera.Infraestructure.repository.activity;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proriberaapp.ribera.Domain.Interfaces.ActivityRoomProjection;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActivityDashboardRepository extends R2dbcRepository<BookingEntity, Integer> {
        @Query("""
                        SELECT COUNT(DISTINCT b.bookingid)
                        FROM booking b
                        JOIN paymentbook pb ON pb.bookingid = b.bookingid
                        WHERE pb.paymentstateid = 2
                        AND DATE(b.daybookinginit) <= :date
                        AND DATE(b.daybookingend) >= :date
                        """)
        Mono<Integer> countTotalPaid(@Param("date") LocalDateTime date);

        @Query("""
                        SELECT COUNT(DISTINCT b.bookingid)
                        FROM booking b
                        WHERE DATE(b.daybookinginit) = :date
                        """)
        Mono<Integer> countTotalCheckIn(@Param("date") LocalDateTime date);

        @Query("""
                        SELECT COUNT(DISTINCT b.bookingid)
                        FROM booking b
                        WHERE DATE(b.daybookingend) = :date
                        """)
        Mono<Integer> countTotalCheckOut(@Param("date") LocalDateTime date);

        @Query("""
                        SELECT COUNT(DISTINCT b.bookingid)
                        FROM booking b
                        WHERE b.bookingstateid = 3
                        AND DATE(b.daybookinginit) <= :date
                        AND DATE(b.daybookingend) >= :date
                        """)
        Mono<Integer> countTotalReservations(@Param("date") LocalDateTime date);

        @Query("""
                        SELECT COUNT(DISTINCT b.bookingid)
                        FROM booking b
                        WHERE b.bookingstateid = 1
                        AND DATE(b.daybookinginit) <= :date
                        AND DATE(b.daybookingend) >= :date
                            """)
        Mono<Integer> countTotalNoShow(@Param("date") LocalDateTime date);

        @Query("""
                        SELECT COUNT(DISTINCT r.roomid)
                        FROM room r
                        WHERE r.roomid NOT IN(
                            SELECT DISTINCT ro.roomid
                            FROM roomoffer ro
                            JOIN booking b
                            ON b.roomofferid = ro.roomofferid
                            WHERE DATE(b.daybookinginit) <= :date
                            AND DATE(b.daybookingend) >= :date
                            )
                        """)

        Mono<Integer> countTotalAvailables(@Param("date") LocalDateTime date);
}
