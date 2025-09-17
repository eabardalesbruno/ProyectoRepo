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
                        """)

        Mono<Integer> countTotalAvailables(@Param("date") LocalDateTime date);

        @Query("""
                        SELECT
                            r.roomid,
                            r.roomnumber,
                            r.roomname,
                            rt.roomtypename,
                            rt.categoryname,
                            b.bookingid,
                            b.daybookinginit,
                            b.daybookingend,
                            b.numberadults,
                            b.numberchildren,
                            b.numberbabies,
                            b.numberadultsextra,
                            b.numberadultsmayor,
                            b.bookingstateid,
                            uc.firstname,
                            uc.lastname,
                            uc.isuserinclub,
                            CASE
                                WHEN b.bookingid IS NULL THEN 'DISPONIBLE'
                                WHEN b.bookingstateid = 1 THEN 'NO SHOW'
                                WHEN b.bookingstateid = 3 THEN 'RESERVADO'
                                WHEN pb.paymentstateid = 2 THEN 'PAGADO'
                                ELSE 'OTRO'
                            END as status,
                            pb.paymentstateid,
                            pm.description as payment_method,
                            bf.bookingfeedingid IS NOT NULL as has_feeding
                        FROM room r
                        LEFT JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
                        LEFT JOIN roomoffer ro ON ro.roomid = r.roomid
                        LEFT JOIN booking b ON b.roomofferid = ro.roomofferid
                            AND DATE(b.daybookinginit) <= :date
                            AND DATE(b.daybookingend) >= :date
                        LEFT JOIN userclient uc ON uc.userclientid = b.userclientid
                        LEFT JOIN paymentbook pb ON pb.bookingid = b.bookingid
                        LEFT JOIN paymentmethod pm ON pm.paymentmethodid = pb.paymentmethodid
                        LEFT JOIN booking_feeding bf ON bf.bookingid = b.bookingid
                        ORDER BY r.roomnumber
                        LIMIT :size OFFSET :offset
                        """)
        Flux<ActivityRoomProjection> findAllRooms(
                        @Param("date") LocalDateTime date,
                        @Param("size") int size,
                        @Param("offset") int offset);

        @Query("""
                        SELECT COUNT(DISTINCT r.roomid)
                        FROM room r
                        LEFT JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
                        LEFT JOIN roomoffer ro ON ro.roomid = r.roomid
                        LEFT JOIN booking b ON b.roomofferid = ro.roomofferid
                                AND DATE(b.daybookinginit) <= :date
                                AND DATE(b.daybookingend) >= :date
                        LEFT JOIN userclient uc ON uc.userclientid = b.userclientid
                        LEFT JOIN paymentbook pb ON bookingid = b.bookingid
                        LEFT JOIN paymentmethod pm ON pm.paymentmethodid = pb.paymentmethodid
                        LEFT JOIN booking_feeding bf ON bf.bookingid = b.bookingid
                                """)
        Mono<Long> countAllRooms(@Param("date") LocalDateTime date);
}
