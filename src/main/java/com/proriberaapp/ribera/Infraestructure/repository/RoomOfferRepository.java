package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomOfferRepository extends R2dbcRepository<RoomOfferEntity, Integer> {
  Mono<RoomOfferEntity> findByRoomIdAndState(Integer roomId, Integer state);

  Flux<RoomOfferEntity> findAllByRoomId(Integer roomId);

  Flux<RoomOfferEntity> findAllByRoomIdInAndOfferTypeIdIn(List<RoomOfferEntity> roomOfferEntity,
      List<RoomOfferEntity> roomOfferEntity1);

  // @Query("SELECT cost FROM roomoffer WHERE roomofferid = :id")
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
  Flux<RoomOfferEntity> findByFilters(Integer roomTypeId, String capacity, LocalDateTime offerTimeInit,
      LocalDateTime offerTimeEnd);

  /*
   * @Query("""
   * SELECT v.*
   * FROM viewroomofferreturn v
   * 
   * WHERE (:roomTypeId IS NULL OR v.roomtypeid = :roomTypeId)
   * AND (
   * (:offerTimeInit IS NULL AND :offerTimeEnd IS NULL) OR
   * (:offerTimeInit IS NOT NULL AND :offerTimeEnd IS NULL AND
   * DATE(:offerTimeInit) BETWEEN DATE(v.offertimeinit) AND DATE(v.offertimeend)
   * ) OR
   * (:offerTimeInit IS NOT NULL AND :offerTimeEnd IS NOT NULL AND
   * (
   * DATE(v.offertimeend) BETWEEN DATE(:offerTimeInit) AND DATE(:offerTimeEnd) OR
   * DATE(:offerTimeInit) BETWEEN DATE(v.offertimeinit) AND DATE(v.offertimeend)
   * OR
   * DATE(:offerTimeEnd) BETWEEN DATE(v.offertimeinit) AND DATE(v.offertimeend)
   * )
   * )
   * )
   * AND (:infantCapacity IS NULL OR v.infantcapacity >= :infantCapacity)
   * AND (:kidCapacity IS NULL OR v.kidcapacity >= :kidCapacity)
   * AND (:adultCapacity IS NULL OR v.adultcapacity >= :adultCapacity)
   * AND (:adultMayorCapacity IS NULL OR v.adultmayorcapacity >=
   * :adultMayorCapacity)
   * AND (:adultExtra IS NULL OR v.adultextra >= :adultExtra)
   * 
   * ORDER BY v.roomofferid ASC
   * """)
   */
  @Query("""
             SELECT v.*,case
             when :isFirstState  then true
             when  v.categoryname='DEPARTAMENTO' and  (:adultCapacity+:adultMayorCapacity+:adultExtra+:kidCapacity)>=v.mincapacity then true
             when v.categoryname='MATRIMONIAL' and  (:adultCapacity+:adultMayorCapacity+:adultExtra+:kidCapacity)>=v.mincapacity and v.adultextra+v.adultcapacity+v.adultmayorcapacity>=(:adultCapacity+:adultMayorCapacity+:adultExtra) and v.infantcapacity+v.kidcapacity>=(:infantCapacity+:kidCapacity) then true
             when v.categoryname='DOBLE' and  (:adultCapacity+:adultMayorCapacity+:adultExtra+:kidCapacity)>=v.mincapacity and v.adultextra+v.adultcapacity+v.adultmayorcapacity>=(:adultCapacity+:adultMayorCapacity+:adultExtra) and v.infantcapacity+v.kidcapacity>=(:infantCapacity+:kidCapacity) then true
             else false
             end as isbooking
           FROM viewroomofferreturn v
          JOIN quotation_day qd on qd.idquotation=v.quotation_id
          join "day" d on d."id"=qd.idday and to_char(now(),'d')::integer=d.numberofweek
             WHERE
              	:categoryName is  null or 	 v.categoryname=:categoryName
      		 and (
      ('DEPARTAMENTO'=v.categoryname	 and

      v.adultcapacity+v.adultextra+v.adultmayorcapacity+v.infantcapacity+v.kidcapacity>=(:adultCapacity+:kidCapacity+:infantCapacity+:adultMayorCapacity+:adultExtra))

      or

      ( 'DOBLE'=v.categoryname
      and v.adultcapacity+v.adultextra+v.adultmayorcapacity>=(:adultCapacity+:adultMayorCapacity+:adultExtra)
      and v.infantcapacity+v.kidcapacity>=(:infantCapacity+:kidCapacity))
      or
      ( 'MATRIMONIAL'=v.categoryname
      and v.adultextra+v.adultcapacity+v.adultmayorcapacity>=(:adultCapacity+:adultMayorCapacity+:adultExtra) and v.infantcapacity+v.kidcapacity>=(:infantCapacity+:kidCapacity)

      ))
               and (
                     (:offerTimeInit IS NULL AND :offerTimeEnd IS NULL) OR
                     (:offerTimeInit IS NOT NULL AND :offerTimeEnd IS NULL AND
                      DATE(:offerTimeInit) BETWEEN DATE(v.offertimeinit) AND DATE(v.offertimeend)
                     ) OR
                     (:offerTimeInit IS NOT NULL AND :offerTimeEnd IS NOT NULL AND
                      (
                         DATE(v.offertimeend) BETWEEN DATE(:offerTimeInit) AND DATE(:offerTimeEnd) OR
                         DATE(:offerTimeInit) BETWEEN DATE(v.offertimeinit) AND DATE(v.offertimeend) OR
                         DATE(:offerTimeEnd) BETWEEN DATE(v.offertimeinit) AND DATE(v.offertimeend)
                      )
                     )
                   )

      and (:roomTypeId IS NULL OR v.roomtypeid = :roomTypeId)
           """)
  Flux<ViewRoomOfferReturn> 
  findFilteredV2(
      Boolean isFirstState, Integer roomTypeId, String categoryName,
      LocalDate offerTimeInit,
      LocalDate offerTimeEnd,
      Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity, Integer adultExtra,
      Integer infantCapacity);

  @Query("""
      SELECT v.*, r.state, r.numberdays
              FROM roomoffer r
              JOIN viewroomofferreturn v ON r.roomofferid = v.roomofferid
             
              WHERE (:roomTypeId IS NULL OR v.roomtypeid = :roomTypeId)
              and  join "day" d on d."id"=qd.idday and to_char(now(),'d')::integer=d.numberofweek
              AND (:infantCapacity IS NULL OR v.infantcapacity >= :infantCapacity)
              AND (:kidCapacity IS NULL OR v.kidcapacity >= :kidCapacity)
              AND (:adultCapacity IS NULL OR v.adultcapacity >= :adultCapacity)
              AND (:adultMayorCapacity IS NULL OR v.adultmayorcapacity >= :adultMayorCapacity)
              AND (:adultExtra IS NULL OR v.adultextra >= :adultExtra)
              AND (
                (:offerTimeInit IS NULL AND :offerTimeEnd IS NULL) OR
                (:offerTimeInit IS NOT NULL AND :offerTimeEnd IS NULL AND
                 DATE(:offerTimeInit) BETWEEN DATE(r.offertimeinit) AND DATE(r.offertimeend)
                ) OR
                (:offerTimeInit IS NOT NULL AND :offerTimeEnd IS NOT NULL AND
                 (
                    DATE(r.offertimeend) BETWEEN DATE(:offerTimeInit) AND DATE(:offerTimeEnd) OR
                    DATE(:offerTimeInit) BETWEEN DATE(r.offertimeinit) AND DATE(r.offertimeend) OR
                    DATE(:offerTimeEnd) BETWEEN DATE(r.offertimeinit) AND DATE(r.offertimeend)
                 )
                )
              )
              ORDER BY r.roomofferid ASC
                           """)
  Flux<ViewRoomOfferReturn> findFiltered(Integer roomTypeId, LocalDate offerTimeInit,
      LocalDate offerTimeEnd,
      Integer infantCapacity, Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity,
      Integer adultExtra);

  @Query("""
      SELECT * FROM viewroomofferreturn
      WHERE (:roomId IS NULL OR roomid = :roomId)
      AND (:roomOfferId IS NULL OR roomofferid = :roomOfferId)
      AND (:roomTypeId IS NULL OR roomtypeid = :roomTypeId)
      AND (:typeRoom IS NULL OR typeroom = :typeRoom)
      """)
  Flux<ViewRoomOfferReturn> viewRoomOfferReturn(SearchFiltersRoomOffer filters);

}
