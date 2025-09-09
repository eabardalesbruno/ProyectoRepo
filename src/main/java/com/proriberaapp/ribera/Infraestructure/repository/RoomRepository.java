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
    LEFT JOIN paymentbook pb ON b.bookingid = pb.bookingid
    LEFT JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
    WHERE (:roomnumber IS NULL OR r.roomnumber = :roomnumber)
      AND (b.daybookingend::DATE >= CAST(:daybookingend AS DATE))
      AND (b.daybookinginit::DATE <= CAST(:daybookinginit AS DATE))
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
        SELECT distinct r.roomnumber
        FROM room r
        JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid AND rt.roomtypeid = :roomtypeid
        WHERE :roomNumber IS NULL OR r.roomnumber = :roomNumber
        ORDER BY r.roomnumber
    """)
    Flux<RoomDto> findRoomByRoomNumberAndRoomTypeId(@Param("roomNumber") String roomNumber, @Param("roomtypeid") Integer roomtypeid);
/*
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
            LEFT JOIN paymentbook pb ON b.bookingid = pb.bookingid
            LEFT JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
            JOIN bookingstate bs ON bs.bookingstateid = b.bookingstateid
            WHERE (b.daybookingend::DATE >= CAST(:daybookingend AS DATE))
              AND (b.daybookinginit::DATE <= CAST(:daybookinginit AS DATE))
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
            WITH room_with_bookingstate AS (
                SELECT
                    r.roomnumber,
                    ro.roomofferid,
                    b.bookingid,
                    CASE
                        WHEN ps.paymentstatename = 'ACEPTADO' AND bs.bookingstatename = 'ACEPTADO' THEN 'ACEPTADO'
                        WHEN b.bookingid IS NOT NULL THEN bs.bookingstatename
                        WHEN ps.paymentstatename IS NOT NULL THEN ps.paymentstatename
                        ELSE NULL
                    END AS bookingstate
                FROM room r
                LEFT JOIN roomoffer ro ON r.roomid = ro.roomid
                LEFT JOIN booking b ON ro.roomofferid = b.roomofferid
                    AND (b.daybookingend::DATE >= CAST(:daybookingend AS DATE))
                    AND (b.daybookinginit::DATE <= CAST(:daybookinginit AS DATE))
                LEFT JOIN paymentbook pb ON b.bookingid = pb.bookingid
                LEFT JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
                LEFT JOIN bookingstate bs ON bs.bookingstateid = b.bookingstateid
            ),
            
            -- Clasificamos cada roomnumber según la prioridad del estado
            room_status_ranked AS (
                SELECT
                    roomnumber,
                    MAX(
                        CASE
                            WHEN bookingstate = 'ACEPTADO' THEN 3
                            WHEN bookingstate IS NOT NULL THEN 2
                            ELSE 1
                        END
                    ) AS rank
                FROM room_with_bookingstate
                GROUP BY roomnumber
            ),
            
            -- Traducimos los rangos a nombres de estado
            room_status_labeled AS (
                SELECT
                    roomnumber,
                    CASE rank
                        WHEN 3 THEN 'ACEPTADO'
                        WHEN 2 THEN 'PENDIENTE'
                        ELSE 'LIBRE'
                    END AS bookingstate
                FROM room_status_ranked
            )
            
            -- Contamos cuántos roomnumbers están en cada estado
            SELECT
                bookingstate,
                COUNT(*) AS total,
                ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (), 2) AS percentage
            FROM room_status_labeled
            GROUP BY bookingstate
            ORDER BY bookingstate
""")
    Flux<BookingStateStatsDto> findBookingStateStats(
            @Param("daybookinginit") String daybookinginit,
            @Param("daybookingend") String daybookingend
    );
*/

    @Query(value = """
            WITH room_with_bookingstate AS (
                SELECT
                    r.roomnumber,
                    r.roomid,
                    ro.roomofferid,
                    ro.offername,
                    b.bookingid,
                    b.daybookinginit,
                    b.daybookingend,
                    b.createdat,
                    (b.numberadults + COALESCE(b.numberadultsmayor, 0) + COALESCE(b.numberadultsextra, 0)) AS numberadults,
                    b.numberchildren,
                    b.numberbabies,
                    bs.bookingstatename,
                    ps.paymentstatename,
                    CASE
                        -- Prioridad 1: Ocupado (el estado más relevante para el día de hoy)
                        WHEN bs.bookingstatename = 'OCUPADO' THEN 'OCUPADO'
                        -- Prioridad 2: Aceptado (reserva y pago confirmados)
                        WHEN ps.paymentstatename = 'ACEPTADO' AND bs.bookingstatename = 'ACEPTADO' THEN 'ACEPTADO'
                        -- Prioridad 3: Pendiente
                        WHEN bs.bookingstatename = 'PENDIENTE' THEN 'PENDIENTE'
                        -- Prioridad 4: Limpieza
                        WHEN bs.bookingstatename = 'LIMPIEZA' THEN 'LIMPIEZA'
                        -- Prioridad 5: Anulado
                        WHEN bs.bookingstatename = 'ANULADO' THEN 'ANULADO'
                        -- Prioridad 6: Finalizado
                        WHEN bs.bookingstatename = 'FINALIZADO' THEN 'FINALIZADO'
                        -- Prioridad 7: Rechazado
                        WHEN bs.bookingstatename = 'RECHAZADO' THEN 'RECHAZADO'
                        -- Si no hay bookingstate, usa el estado de pago.
                        WHEN ps.paymentstatename = 'ACEPTADO' THEN 'ACEPTADO' -- Pago Aceptado sin estado de reserva
                        WHEN ps.paymentstatename = 'PENDIENTE' THEN 'PENDIENTE' -- Pago Pendiente sin estado de reserva
                        -- Estado por defecto: Si no hay reserva ni pago, es LIBRE.
                        ELSE 'LIBRE'
                    END AS final_state,
                    -- Ranking para identificar la reserva que determina el estado final
                    ROW_NUMBER() OVER (
                        PARTITION BY r.roomnumber
                        ORDER BY
                            CASE
                                -- OCUPADO es la máxima prioridad para el estado de la habitación
                                WHEN bs.bookingstatename = 'OCUPADO' THEN 1
                                -- PAGO ACEPTADO y RESERVA ACEPTADA (segunda prioridad)
                                WHEN ps.paymentstatename = 'ACEPTADO' AND bs.bookingstatename = 'ACEPTADO' THEN 2
                                -- RESERVADO (PENDIENTE)
                                WHEN bs.bookingstatename = 'PENDIENTE' OR ps.paymentstatename = 'PENDIENTE' THEN 3
                                -- LIMPIEZA
                                WHEN bs.bookingstatename = 'LIMPIEZA' THEN 4
                                -- ANULADO
                                WHEN bs.bookingstatename = 'ANULADO' THEN 5
                                -- FINALIZADO
                                WHEN bs.bookingstatename = 'FINALIZADO' THEN 6
                                -- RECHAZADO
                                WHEN bs.bookingstatename = 'RECHAZADO' THEN 7
                                ELSE 8
                            END,
                            b.createdat DESC
                    ) AS state_rank
                FROM room r
                LEFT JOIN roomoffer ro ON r.roomid = ro.roomid
                LEFT JOIN booking b ON ro.roomofferid = b.roomofferid
                    AND (b.daybookingend::DATE >= CAST(:daybookingend AS DATE))
                    AND (b.daybookinginit::DATE <= CAST(:daybookinginit AS DATE))
                LEFT JOIN paymentbook pb ON b.bookingid = pb.bookingid
                LEFT JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
                LEFT JOIN bookingstate bs ON bs.bookingstateid = b.bookingstateid
                WHERE (:roomnumber IS NULL OR r.roomnumber = :roomnumber)
                  AND (:bookingstateid IS NULL OR b.bookingstateid = :bookingstateid OR b.bookingid IS NULL)
            ),
            
            room_final_state AS (
                SELECT
                    roomnumber,
                    roomid,
                    roomofferid,
                    offername,
                    bookingid,
                    daybookinginit,
                    daybookingend,
                    createdat,
                    numberadults,
                    numberchildren,
                    numberbabies,
                    bookingstatename,
                    paymentstatename,
                    final_state,
                    state_rank
                FROM room_with_bookingstate
                WHERE state_rank = 1
            )
            
            SELECT
                r.roomid,
                r.roomnumber,
                r.roomofferid,
                r.offername,
                r.daybookinginit,
                r.daybookingend,
                r.bookingid,
                (r.daybookingend::DATE - r.daybookinginit::DATE) AS numdays,
                pb.paymentstateid,
                r.final_state AS bookingstate,
                r.createdat,
                r.numberadults,
                r.numberchildren,
                r.numberbabies,
                r.bookingstatename,
                r.paymentstatename,
                ps.paymentstatename AS payment_state_name
            FROM room_final_state r
            LEFT JOIN paymentbook pb ON r.bookingid = pb.bookingid
            LEFT JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
            WHERE r.final_state != 'LIBRE'
            ORDER BY
                CASE
                    -- El orden de la interfaz debe reflejar el orden de la lógica de negocio
                    WHEN r.final_state = 'OCUPADO' THEN 1
                    WHEN r.final_state = 'ACEPTADO' THEN 2
                    WHEN r.final_state = 'PENDIENTE' THEN 3
                    WHEN r.final_state = 'LIMPIEZA' THEN 4
                    WHEN r.final_state = 'ANULADO' THEN 5
                    WHEN r.final_state = 'FINALIZADO' THEN 6
                    WHEN r.final_state = 'RECHAZADO' THEN 7
                    ELSE 8
                END,
                r.roomnumber
            LIMIT :size OFFSET :page;
            """)
    Flux<RoomDetailDto> findAllViewRoomsDetailActivities(
            @Param("daybookinginit") String daybookinginit,
            @Param("daybookingend") String daybookingend,
            @Param("roomnumber") String roomnumber,
            @Param("bookingstateid") Integer bookingstateid,
            @Param("size") Integer size,
            @Param("page") Integer page);

    @Query(value = """
            WITH room_with_bookingstate AS (
                SELECT
                    r.roomnumber,
                    ro.roomofferid,
                    b.bookingid,
                    CASE
                        -- Prioridad 1: Si está ocupado, ese es el estado más relevante para el día de hoy.
                        WHEN bs.bookingstatename = 'OCUPADO' THEN 'OCUPADO'
                        -- Prioridad 2: Estado de pago y reserva confirmados.
                        WHEN ps.paymentstatename = 'ACEPTADO' AND bs.bookingstatename = 'ACEPTADO' THEN 'ACEPTADO'
                        -- Prioridad 3: Si hay un booking id, usa el estado de la reserva.
                        WHEN b.bookingid IS NOT NULL THEN bs.bookingstatename
                        -- Prioridad 4: Si hay estado de pago pero no de reserva, usa el de pago.
                        WHEN ps.paymentstatename IS NOT NULL THEN ps.paymentstatename
                        -- Estado por defecto: Si no hay reserva ni pago, es NULL.
                        ELSE NULL
                    END AS bookingstate
                FROM room r
                LEFT JOIN roomoffer ro ON r.roomid = ro.roomid
                LEFT JOIN booking b ON ro.roomofferid = b.roomofferid
                    AND (b.daybookingend::DATE >= CAST(:daybookingend AS DATE))
                    AND (b.daybookinginit::DATE <= CAST(:daybookinginit AS DATE))
                LEFT JOIN paymentbook pb ON b.bookingid = pb.bookingid
                LEFT JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
                LEFT JOIN bookingstate bs ON bs.bookingstateid = b.bookingstateid
            ),
            room_status_ranked AS (
                SELECT
                    roomnumber,
                    MAX(
                        CASE bookingstate
                            WHEN 'OCUPADO' THEN 6
                            WHEN 'ACEPTADO' THEN 5
                            WHEN 'PENDIENTE' THEN 4
                            WHEN 'LIMPIEZA' THEN 3
                            WHEN 'FINALIZADO' THEN 2
                            WHEN 'ANULADO' THEN 1
                            WHEN 'RECHAZADO' THEN 0
                            ELSE -1
                        END
                    ) AS rank
                FROM room_with_bookingstate
                GROUP BY roomnumber
            ),
            room_status_labeled AS (
                SELECT
                    roomnumber,
                    CASE rank
                        WHEN 6 THEN 'OCUPADO'
                        WHEN 5 THEN 'ACEPTADO'
                        WHEN 4 THEN 'PENDIENTE'
                        WHEN 3 THEN 'LIMPIEZA'
                        WHEN 2 THEN 'FINALIZADO'
                        WHEN 1 THEN 'ANULADO'
                        WHEN 0 THEN 'RECHAZADO'
                        ELSE 'LIBRE'
                    END AS bookingstate
                FROM room_status_ranked
            )
            SELECT
                bookingstate,
                COUNT(*) AS total,
                ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (), 2) AS percentage
            FROM room_status_labeled
            GROUP BY bookingstate
            ORDER BY bookingstate;
            """)
    Flux<BookingStateStatsDto> findBookingStateStats(
            @Param("daybookinginit") String daybookinginit,@Param("daybookingend") String daybookingend);

    @Query("""
    select distinct roomnumber from room order by roomnumber
    """)
    Flux<RoomEntity> getAllNumRooms();
}
