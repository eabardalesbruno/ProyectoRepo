package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BedroomReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingStateStatsDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RoomDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.dto.RoomDto;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomRepository extends R2dbcRepository<RoomEntity, Integer>{
    Mono<RoomEntity> findByRoomName(String roomName);
    Flux<RoomEntity> findAllByRoomNameIn(List<String> roomName);
    Flux<RoomEntity> findAllByRoomTypeId(Integer roomTypeId);
    Flux<RoomEntity> findAllByOrderByRoomIdAsc();
    @Query("SELECT * FROM ViewRoomReturn")
    Flux<ViewRoomReturn> findAllViewRoomReturn();

    @Query("SELECT * FROM ViewBedroomReturn WHERE roomid = :roomid")
    Flux<BedroomReturn> findAllViewBedroomReturn(@Param("roomid") Integer roomid);

    @Query("""
        SELECT roomnumber
        FROM room GROUP BY roomnumber ORDER BY roomnumber
    """)
    Flux<String> findAllViewRooms();

    @Query("""
            SELECT DISTINCT r.roomnumber
            FROM room r
            LEFT JOIN roomoffer ro ON r.roomid = ro.roomid
            LEFT JOIN booking b ON ro.roomofferid = b.roomofferid
            WHERE (:roomtypeid IS NULL OR r.roomtypeid = :roomtypeid)
            AND (:bookingid IS NULL OR b.bookingid = :bookingid)
            ORDER BY r.roomnumber
            """)
    Flux<String> findFilteredRooms(
            @Param("roomtypeid") Integer roomtypeid,
            @Param("bookingid") Integer bookingid);

    @Query("""
        select r.* from booking b
        join roomoffer ro on b.roomofferid = ro.roomofferid
        join room r on ro.roomid = r.roomid
        where b.bookingid = :bookingId
    """)
    Mono<RoomEntity> getRoomNameByBookingId(@Param("bookingId") Integer bookingId);

    @Query("""
     SELECT DISTINCT ON (b.bookingid)
             r.roomid,
             r.roomnumber,
             ro.roomofferid,
             b.daybookinginit,
             b.daybookingend,
             pb.paymentstateid,
             ps.paymentstatename,
             b.bookingid,
             (b.daybookingend::DATE - b.daybookinginit::DATE) AS numdays,
             (SELECT bs.bookingstatename
              FROM bookingstate bs
              WHERE bs.bookingstateid = b.bookingstateid) AS bookingstate
    FROM room r
    JOIN roomoffer ro ON r.roomid = ro.roomid
    JOIN booking b ON ro.roomofferid = b.roomofferid
    JOIN paymentbook pb ON b.bookingid = pb.bookingid
    JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
    WHERE (:roomnumber IS NULL OR r.roomnumber = :roomnumber)
      AND (:daybookinginit IS NULL OR b.daybookingend::DATE >= CAST(:daybookinginit AS DATE))
      AND (:daybookingend IS NULL OR b.daybookinginit::DATE <= CAST(:daybookingend AS DATE))
      AND (:roomtypeid IS NULL OR r.roomtypeid = :roomtypeid)
      AND (:numberadults IS NULL OR b.numberadults = :numberadults)
      AND (:numberchildren IS NULL OR b.numberchildren = :numberchildren)
      AND (:numberbabies IS NULL OR b.numberbabies = :numberbabies)
      AND (:bookingid IS NULL OR b.bookingid != :bookingid)
    ORDER BY b.bookingid,
             CASE WHEN ps.paymentstatename = 'ACEPTADO' THEN 1 ELSE 2 END
""")
    Flux<RoomDetailDto> findAllViewRoomsDetail(
            @Param("daybookinginit") String daybookinginit,
            @Param("daybookingend") String daybookingend,
            @Param("roomnumber") String roomnumber,
            @Param("roomtypeid") Integer roomtypeid,
            @Param("numberadults") Integer numberadults,
            @Param("numberchildren") Integer numberchildren,
            @Param("numberbabies") Integer numberbabies,
            @Param("bookingid") Integer bookingid);

    @Query("""
        SELECT distinct r.roomnumber
        FROM room r
        JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid AND rt.roomtypeid = :roomtypeid
        ORDER BY r.roomnumber
    """)
    Flux<RoomDto> findRoomByRoomTypeId(@Param("roomtypeid") Integer roomtypeid);

    @Query("""
        SELECT *
        FROM (
            SELECT DISTINCT ON (b.bookingid)
                r.roomid,
                r.roomnumber,
                ro.roomofferid,
                ro.offername,
                b.daybookinginit,
                b.daybookingend,
                b.bookingid,
                (b.daybookingend::DATE - b.daybookinginit::DATE) AS numdays,
                pb.paymentstateid,
                CASE
                    WHEN ps.paymentstatename = 'ACEPTADO' AND bs.bookingstatename = 'ACEPTADO'
                        THEN 'ACEPTADO'
                    ELSE bs.bookingstatename
                END AS bookingstate,
                b.createdat,
                (b.numberadults + b.numberadultsmayor + b.numberadultsextra) AS numberadults,
                b.numberchildren,
                b.numberbabies
            FROM room r
            JOIN roomoffer ro ON r.roomid = ro.roomid
            JOIN booking b ON ro.roomofferid = b.roomofferid
            JOIN paymentbook pb ON b.bookingid = pb.bookingid
            JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
            JOIN bookingstate bs ON bs.bookingstateid = b.bookingstateid
            WHERE (:daybookingend IS NULL OR b.daybookingend::DATE >= CAST(:daybookingend AS DATE))
              AND (:daybookinginit IS NULL OR b.daybookinginit::DATE <= CAST(:daybookinginit AS DATE))
              AND (:roomnumber IS NULL OR r.roomnumber = :roomnumber)
              AND (:bookingstateid IS NULL OR b.bookingstateid = :bookingstateid)
            ORDER BY b.bookingid,
                     CASE WHEN ps.paymentstatename = 'ACEPTADO' THEN 1 ELSE 2 END
        ) AS result
        ORDER BY bookingid
        LIMIT :size OFFSET :page        
    """)
    Flux<RoomDetailDto> findAllViewRoomsDetailActivities(
            @Param("daybookinginit") String daybookinginit,
            @Param("daybookingend") String daybookingend,
            @Param("roomnumber") String roomnumber,
            @Param("bookingstateid") Integer bookingstateid,
            @Param("size") Integer size,
            @Param("page") Integer page);

    @Query("""
    WITH filtered_bookings AS (
        SELECT DISTINCT ON (b.bookingid)
            b.bookingid,
            CASE
                WHEN ps.paymentstatename = 'ACEPTADO' AND bs.bookingstatename = 'ACEPTADO' THEN 'ACEPTADO'
                ELSE ps.paymentstatename
            END AS bookingstate
        FROM room r
        JOIN roomoffer ro ON r.roomid = ro.roomid
        JOIN booking b ON ro.roomofferid = b.roomofferid
        JOIN paymentbook pb ON b.bookingid = pb.bookingid
        JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
        JOIN bookingstate bs ON bs.bookingstateid = b.bookingstateid
        WHERE (:daybookingend IS NULL OR b.daybookingend::DATE >= CAST(:daybookingend AS DATE))
          AND (:daybookinginit IS NULL OR b.daybookinginit::DATE <= CAST(:daybookinginit AS DATE))
        ORDER BY b.bookingid,
                 CASE WHEN ps.paymentstatename = 'ACEPTADO' THEN 1 ELSE 2 END
    )
    
    SELECT
        bookingstate,
        COUNT(*) AS total,
        ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (), 2) AS percentage
    FROM filtered_bookings
    GROUP BY bookingstate
""")
    Flux<BookingStateStatsDto> findBookingStateStats(
            @Param("daybookinginit") String daybookinginit,
            @Param("daybookingend") String daybookingend
    );

    @Query("""
    select distinct roomnumber from room order by roomnumber
    """)
    Flux<RoomEntity> getAllNumRooms();

}
