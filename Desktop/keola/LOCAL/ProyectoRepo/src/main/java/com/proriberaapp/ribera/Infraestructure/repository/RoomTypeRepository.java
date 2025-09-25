package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.RoomTypeDto;
import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.Domain.entities.RoomTypeViewEntity;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomTypeRepository extends R2dbcRepository<RoomTypeEntity, Integer> {
    Mono<RoomTypeEntity> findByRoomTypeName(String roomTypeName);

    Flux<RoomTypeEntity> findAllByRoomTypeNameIn(List<String> roomTypeNames);

    Flux<RoomTypeEntity> findAllByOrderByRoomTypeIdAsc();

    @Query("""
            SELECT rt.*,rs.roomstatedescription,rs.roomstatename,crt.* from
            roomtype rt
            join roomstate rs on rs.roomstateid=rt.roomstateid
            join category_room_type crt on crt."id"=rt.categoryid
            order by rt.roomtypeid asc
            """)
    Flux<RoomTypeViewEntity> findAllRoomTypeView();

    @Query("""
            SELECT * FROM roomtype
            WHERE :roomtypeid IS NULL OR roomtypeId = :roomtypeid
            ORDER BY roomtypename
            """)
    Flux<RoomTypeDto> findAllRoomTypes(@Param("roomtypeId") Integer roomtypeId);

}
