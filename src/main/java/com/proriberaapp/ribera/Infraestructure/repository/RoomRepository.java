package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BedroomReturn;
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
            AND (
                    -- Si NO se especifican fechas, devuelve todas las habitaciones
                    (:daybookinginit IS NULL AND :daybookingend IS NULL)
                    OR
                    -- Si hay fechas, devuelve solo habitaciones con reservas en ese rango
                    (
                        b.daybookinginit <= :daybookingend::TIMESTAMP
                        AND b.daybookingend >= :daybookinginit::TIMESTAMP
                    )
            )
            ORDER BY r.roomnumber
            """)
    Flux<String> findFilteredRooms(
            @Param("daybookinginit") String daybookinginit,
            @Param("daybookingend") String daybookingend,
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
        SELECT r.roomid, r.roomnumber, ro.roomofferid, b.daybookinginit, b.daybookingend, pb.paymentstateid, ps.paymentstatename, b.bookingid, (b.daybookingend::DATE - b.daybookinginit::DATE) numdays,
           (SELECT bs.bookingstatename FROM bookingstate bs WHERE bs.bookingstateid = b.bookingstateid) bookingstate
        FROM room r
        JOIN roomoffer ro ON r.roomid = ro.roomid
        JOIN booking b ON ro.roomofferid = b.roomofferid
        JOIN paymentbook pb ON b.bookingid = pb.bookingid
        JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
        WHERE r.roomnumber = :roomnumber
        AND (:bookingid IS NULL OR b.bookingid = :bookingid)
        AND (:daybookingend IS NULL OR b.daybookingend <= :daybookingend::TIMESTAMP)
        AND (:daybookinginit IS NULL OR b.daybookinginit >= :daybookinginit::TIMESTAMP)
        AND (:roomtypeid IS NULL OR r.roomtypeid = :roomtypeid)
        AND (:numberadults IS NULL OR b.numberadults = :numberadults)
    	AND (:numberchildren IS NULL OR b.numberchildren = :numberchildren)
    	AND (:numberbabies IS NULL OR b.numberbabies = :numberbabies)
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

}
