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
    Mono<RoomOfferEntity> findByRoomIdAndOfferTypeId(Integer roomId, Integer offerTypeId);
    Flux<RoomOfferEntity> findAllByRoomIdInAndOfferTypeIdIn(List<RoomOfferEntity> roomOfferEntity,List<RoomOfferEntity> roomOfferEntity1);
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

    @Query("SELECT * FROM viewroomofferreturn WHERE " +
            "(:roomTypeId IS NULL OR roomtypeid = :roomTypeId) AND " +
            "(:offerTimeInit IS NULL OR offertimeinit >= :offerTimeInit) AND " +
            "(:offerTimeEnd IS NULL OR offertimeend <= :offerTimeEnd) AND " +
            "(:capacity IS NULL OR capacity = :capacity)")
    Flux<ViewRoomOfferReturn> findFiltered(Integer roomTypeId, LocalDateTime offerTimeInit, LocalDateTime offerTimeEnd, Integer capacity);

    @Query("""
        SELECT * FROM viewroomofferreturn
        WHERE (:roomId IS NULL OR roomid = :roomId)
        AND (:roomOfferId IS NULL OR roomofferid = :roomOfferId)
        AND (:roomTypeId IS NULL OR roomtypeid = :roomTypeId)
        AND (:typeRoom IS NULL OR typeroom = :typeRoom)
        """)
    Flux<ViewRoomOfferReturn> viewRoomOfferReturn(SearchFiltersRoomOffer filters);

}
