package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingStates;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends R2dbcRepository<BookingEntity, Integer> {

        Mono<BookingEntity> findByBookingStateId(BookingEntity bookingEntity);

        Flux<BookingEntity> findAllByBookingStateIdIn(List<BookingEntity> bookingEntity);

        Flux<BookingEntity> findAllByUserClientIdAndBookingStateId(Integer userClientId, Integer bookingStateId);

        @Query("SELECT * FROM booking WHERE roomofferid = :roomOfferId AND ((daybookinginit <= :dayBookingEnd) AND (daybookingend >= :dayBookingInit)) AND bookingStateId != 4")
        Flux<BookingEntity> findExistingBookings(@Param("roomOfferId") Integer roomOfferId,
                        @Param("dayBookingInit") Timestamp dayBookingInit,
                        @Param("dayBookingEnd") Timestamp dayBookingEnd);

        Mono<BookingEntity> findByBookingIdAndUserClientId(Integer userClientId, Integer bookingId);

        @Query("SELECT * FROM ViewBookingReturn WHERE userClientId = :userClientId AND bookingStateId = :bookingStateId")
        Flux<ViewBookingReturn> findAllViewBookingReturnByUserClientIdAndBookingStateId(
                        @Param("userClientId") Integer userClientId, @Param("bookingStateId") Integer bookingStateId);

        @Query("SELECT * FROM ViewBookingReturn WHERE userClientId = :userClientId")
        Flux<ViewBookingReturn> findAllViewBookingReturnByUserClientId(@Param("userClientId") Integer userClientId);

        @Query("SELECT * FROM ViewBookingReturn WHERE bookingId = :bookingId")
        Mono<ViewBookingReturn> findAllViewBookingReturnByBookingId(@Param("bookingId") Integer bookingId);

        @Query("SELECT * FROM booking WHERE bookingstateid = :bookingStateId")
        Flux<ViewBookingReturn> findAllViewBookingReturnByBookingStateId(Integer bookingStateId);

        @Query("SELECT * FROM ViewBookingReturn")
        Flux<ViewBookingReturn> findAllViewBookingReturn();

        @Query("SELECT bookingid, roomofferid, daybookinginit, daybookingend FROM booking WHERE roomofferid = :roomOfferId AND dayBookingEnd >= CURRENT_DATE AND bookingStateId != 4")
        Flux<CalendarDate> findAllCalendarDate(@Param("roomofferid") Integer roomOfferId);

        Mono<BookingEntity> findByBookingId(Integer bookingId);

        @Query("SELECT * FROM ViewBookingReturn WHERE roomTypeId = :roomTypeId AND userClientId = :userClientId AND bookingStateId = :bookingStateId")
        Flux<ViewBookingReturn> findAllViewBookingReturnByRoomTypeIdAndUserClientIdAndBookingStateId(
                        @Param("roomTypeId") Integer roomTypeId, @Param("userClientId") Integer userClientId,
                        @Param("bookingStateId") Integer bookingStateId);

        @Query("SELECT * FROM ViewBookingReturn WHERE dayBookingInit >= :dayBookingInit AND dayBookingEnd <= :dayBookingEnd AND userClientId = :userClientId AND bookingStateId = :bookingStateId")
        Flux<ViewBookingReturn> findAllViewBookingReturnByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(
                        @Param("dayBookingInit") Timestamp dayBookingInit,
                        @Param("dayBookingEnd") Timestamp dayBookingEnd, @Param("userClientId") Integer userClientId,
                        @Param("bookingStateId") Integer bookingStateId);

        @Query("SELECT * FROM ViewBookingReturn WHERE numberAdults = :numberAdults AND numberChildren = :numberChildren AND numberBabies = :numberBabies AND userClientId = :userClientId AND bookingStateId = :bookingStateId")
        Flux<ViewBookingReturn> findAllViewBookingReturnByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(
                        @Param("numberAdults") Integer numberAdults, @Param("numberChildren") Integer numberChildren,
                        @Param("numberBabies") Integer numberBabies, @Param("userClientId") Integer userClientId,
                        @Param("bookingStateId") Integer bookingStateId);

        @Query("SELECT us.firstname, us.lastname, bo.bookingid, rt.roomtypeid, rt.roomtypename, rid.image, " +
                        "r.offertimeinit, r.offertimeend, us.email, bo.costfinal, " +
                        "TO_CHAR(bo.daybookinginit, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookinginit, " +
                        "TO_CHAR(bo.daybookingend, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookingend, " +
                        "rid.capacity, bs.bookingstateid, bs.bookingstatename, bt.bedtypename, bt.bedtypedescription, "
                        +
                        "r.riberapoints, r.inresortpoints, r.points " +
                        "FROM booking bo " +
                        "JOIN roomoffer r ON r.roomofferid = bo.roomofferid " +
                        "JOIN room rid ON rid.roomid = r.roomid " +
                        "JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid " +
                        "JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid " +
                        "JOIN userclient us ON us.userclientid = bo.userclientid " +
                        "JOIN bedroom be ON be.roomid = rid.roomid " +
                        "JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid " +
                        "WHERE bo.bookingstateid = :bookingStateId " +
                        "AND (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId) " +
                        "AND (:capacity IS NULL OR rid.capacity = :capacity) " +
                        "AND (:offertimeInit IS NULL OR :offertimeEnd IS NULL OR " +
                        "bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd) " +
                        "ORDER BY bo.bookingid DESC " +
                        "LIMIT :limit OFFSET :offset")
        Flux<BookingStates> findBookingsByStateIdPaginated(
                        @Param("bookingStateId") Integer bookingStateId,
                        @Param("roomTypeId") Integer roomTypeId,
                        @Param("capacity") Integer capacity,
                        @Param("offertimeInit") LocalDateTime offertimeInit,
                        @Param("offertimeEnd") LocalDateTime offertimeEnd,
                        @Param("limit") int limit,
                        @Param("offset") int offset);

        @Query("SELECT us.firstname, us.lastname, bo.bookingid, rt.roomtypeid, rt.roomtypename, rid.image, " +
                        "r.offertimeinit, r.offertimeend, us.email, bo.costfinal, " +
                        "TO_CHAR(bo.daybookinginit, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookinginit, " +
                        "TO_CHAR(bo.daybookingend, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookingend, " +
                        "bs.bookingstateid, bs.bookingstatename, bt.bedtypename, bt.bedtypedescription,bo.numberadults" +
                        ",bo.numberchildren,bo.numberbabies,bo.numberadultsextra,bo.numberadultsmayor, "
                        +
                        "r.riberapoints, r.inresortpoints, r.points " +
                        "FROM booking bo " +
                        "JOIN roomoffer r ON r.roomofferid = bo.roomofferid " +
                        "JOIN room rid ON rid.roomid = r.roomid " +
                        "JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid " +
                        "JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid " +
                        "JOIN userclient us ON us.userclientid = bo.userclientid " +
                        "JOIN bedroom be ON be.roomid = rid.roomid " +
                        "JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid " +
                        "WHERE bo.bookingstateid = :bookingStateId " +
                        "AND (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId) " +
                        "AND (:offertimeInit IS NULL OR :offertimeEnd IS NULL OR " +
                        "bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd) AND bo.userclientid = :userId "
                        +
                        "ORDER BY bo.bookingid DESC " +
                        "LIMIT :limit OFFSET :offset")
        Flux<BookingStates> findBookingsByStateIdPaginatedAndUserId(
                        @Param("bookingStateId") Integer bookingStateId,
                        @Param("roomTypeId") Integer roomTypeId,
                        @Param("offertimeInit") LocalDateTime offertimeInit,
                        @Param("offertimeEnd") LocalDateTime offertimeEnd,
                        @Param("limit") int limit,
                        @Param("offset") int offset, Integer userId);

        @Query("SELECT count(*) " +
                        "FROM booking bo " +
                        "JOIN roomoffer r ON r.roomofferid = bo.roomofferid " +
                        "JOIN room rid ON rid.roomid = r.roomid " +
                        "JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid " +
                        "JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid " +
                        "JOIN userclient us ON us.userclientid = bo.userclientid " +
                        "JOIN bedroom be ON be.roomid = rid.roomid " +
                        "JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid " +
                        "WHERE bo.bookingstateid = :bookingStateId " +
                        "AND (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId) " +
                        "AND (:capacity IS NULL OR rid.capacity = :capacity) " +
                        "AND (:offertimeInit IS NULL OR :offertimeEnd IS NULL OR " +
                        "bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd) ")
        Mono<Long> countBookingsByStateId(
                        @Param("bookingStateId") Integer bookingStateId,
                        @Param("roomTypeId") Integer roomTypeId,
                        @Param("capacity") Integer capacity,
                        @Param("offertimeInit") LocalDateTime offertimeInit,
                        @Param("offertimeEnd") LocalDateTime offertimeEnd);

        @Query("SELECT count(*) " +
                        "FROM booking bo " +
                        "JOIN roomoffer r ON r.roomofferid = bo.roomofferid " +
                        "JOIN room rid ON rid.roomid = r.roomid " +
                        "JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid " +
                        "JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid " +
                        "JOIN userclient us ON us.userclientid = bo.userclientid " +
                        "JOIN bedroom be ON be.roomid = rid.roomid " +
                        "JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid " +
                        "WHERE bo.bookingstateid = :bookingStateId " +
                        "AND (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId) " +
                        "AND (:offertimeInit IS NULL OR :offertimeEnd IS NULL OR " +
                        "bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd) AND bo.userclientid = :userId ")
        Mono<Long> countBookingsByStateIdAndUserId(
                        @Param("bookingStateId") Integer bookingStateId,
                        @Param("roomTypeId") Integer roomTypeId,
                        @Param("offertimeInit") LocalDateTime offertimeInit,
                        @Param("offertimeEnd") LocalDateTime offertimeEnd, Integer userId);

        Mono<Boolean> existsByRoomOfferId(Integer roomOfferId);

        Mono<Boolean> existsByRoomOfferIdAndBookingStateId(Integer roomOfferId, Integer bookingStateId);

        Flux<BookingEntity> findAllByRoomOfferId(Integer roomOfferId);
}
