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
        SELECT roomnumber
        FROM room GROUP BY roomnumber ORDER BY roomnumber
    """)
    Flux<String> findAllViewRooms();

    @Query("""
        select r.* from booking b
        join roomoffer ro on b.roomofferid = ro.roomofferid
        join room r on ro.roomid = r.roomid
        where b.bookingid = :bookingId
    """)
    Mono<RoomEntity> getRoomNameByBookingId(@Param("bookingId") Integer bookingId);

    @Query("""
        SELECT r.roomid, r.roomnumber, ro.roomofferid, b.daybookinginit, b.daybookingend, pb.paymentstateid, ps.paymentstatename
        FROM room r
        JOIN roomoffer ro ON r.roomid = ro.roomid
        JOIN booking b ON ro.roomofferid = b.roomofferid
        JOIN paymentbook pb ON b.bookingid = pb.bookingid
        JOIN paymentstate ps ON pb.paymentstateid = ps.paymentstateid
        WHERE b.daybookingend >= :daybookingend::TIMESTAMP AND b.daybookinginit <= :daybookinginit::TIMESTAMP
        AND r.roomnumber = :roomnumber
    """)
    Flux<RoomDetailDto> findAllViewRoomsDetail(
            @Param("daybookinginit") String daybookinginit,
            @Param("daybookingend") String daybookingend,
            @Param("roomnumber") String roomnumber);

}
