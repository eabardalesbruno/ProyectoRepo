package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BedroomReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RoomDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
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
        SELECT *, COALESCE((SELECT
        CASE WHEN b.daybookingend < NOW() THEN 'LIBRE'
        ELSE bs.bookingstatename
        END bookingstatename
        FROM booking b
        JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid
        WHERE b.roomofferid = t.roomofferid
        ORDER BY createdat DESC LIMIT 1),'LIBRE') bookingstatename
        FROM
        (SELECT r.roomid, r.roomnumber, ro.roomofferid
        FROM room r
        LEFT JOIN roomoffer ro ON r.roomid = ro.roomid) t
        ORDER BY t.roomnumber;
    """)
    Flux<RoomDetailDto> finAllViewRoomsDetail();

    @Query("""
        select r.* from booking b
        join roomoffer ro on b.roomofferid = ro.roomofferid
        join room r on ro.roomid = r.roomid
        where b.bookingid = :bookingId
    """)
    Mono<RoomEntity> getRoomNameByBookingId(@Param("bookingId") Integer bookingId);

}
