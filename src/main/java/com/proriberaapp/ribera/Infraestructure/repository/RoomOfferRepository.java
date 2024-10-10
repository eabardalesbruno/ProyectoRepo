package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomOfferRepository extends R2dbcRepository<RoomOfferEntity, Integer> {
    Mono<RoomOfferEntity> findByRoomIdAndOfferTypeIdAndState(Integer roomId, Integer offerTypeId, Integer state);

    Flux<RoomOfferEntity> findAllByRoomId(Integer roomId);

    Flux<RoomOfferEntity> findAllByRoomIdInAndOfferTypeIdIn(List<RoomOfferEntity> roomOfferEntity, List<RoomOfferEntity> roomOfferEntity1);

    //@Query("SELECT cost FROM roomoffer WHERE roomofferid = :id")
    Mono<BigDecimal> findCostByRoomOfferId(Integer id);

    @Query("""
            SELECT ro.* FROM roomoffer ro
            JOIN room r ON ro.roomid = r.roomid
            JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
            WHERE (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId)
            AND (:capacity IS NULL OR r.capacity = :capacity)
            AND (:offerTimeInit IS NULL OR ro.offertimeinit >= :offerTimeInit)
            AND (:offerTimeEnd IS NULL OR ro.offertimeend <= :offerTimeEnd)
            """)
    Flux<RoomOfferEntity> findByFilters(Integer roomTypeId, String capacity, LocalDateTime offerTimeInit, LocalDateTime offerTimeEnd);

    @Query("SELECT v.*, r.state, r.numberdays " +
            "FROM roomoffer r " +
            "JOIN viewroomofferreturn v ON r.roomofferid = v.roomofferid " +
            "WHERE (:roomTypeId IS NULL OR v.roomtypeid = :roomTypeId) " +
            "AND (:infantCapacity IS NULL OR v.infantcapacity = :infantCapacity) " +
            "AND (:kidCapacity IS NULL OR v.kidcapacity = :kidCapacity) " +
            "AND (:adultCapacity IS NULL OR v.adultcapacity = :adultCapacity) " +
            "AND (:adultMayorCapacity IS NULL OR v.adultmayorcapacity = :adultMayorCapacity) " +
            "AND (:adultExtra IS NULL OR v.adultextra = :adultExtra) " +
            "AND ( " +
            "  (:offerTimeInit IS NULL OR :offerTimeEnd IS NULL OR " +
            "   (r.offertimeinit <= :offerTimeEnd AND r.offertimeinit >= :offerTimeInit) " +
            "   OR (r.offertimeend <= :offerTimeEnd AND r.offertimeend >= :offerTimeInit) " +
            "   OR (r.offertimeinit <= :offerTimeInit AND r.offertimeend >= :offerTimeEnd)) " +
            ") " +
            "ORDER BY r.roomofferid ASC")
    Flux<ViewRoomOfferReturn> findFiltered(Integer roomTypeId, LocalDateTime offerTimeInit, LocalDateTime offerTimeEnd,
                                           Integer infantCapacity, Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity, Integer adultExtra);

    @Query("""
            SELECT * FROM viewroomofferreturn
            WHERE (:roomId IS NULL OR roomid = :roomId)
            AND (:roomOfferId IS NULL OR roomofferid = :roomOfferId)
            AND (:roomTypeId IS NULL OR roomtypeid = :roomTypeId)
            AND (:typeRoom IS NULL OR typeroom = :typeRoom)
            """)
    Flux<ViewRoomOfferReturn> viewRoomOfferReturn(SearchFiltersRoomOffer filters);

}
