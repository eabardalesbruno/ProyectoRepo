package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingResumenPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingWithPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingStates;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.dto.BookingAndRoomNameDto;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends R2dbcRepository<BookingEntity, Integer> {

  Mono<BookingEntity> findByBookingStateId(BookingEntity bookingEntity);

  Flux<BookingEntity> findAllByBookingStateId(Integer bookingStateId);

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

  @Query("SELECT * FROM ViewBookingReturn WHERE userClientId = :userClientId AND bookingId = :bookingId AND bookingStateId = :bookingStateId")
  Mono<ViewBookingReturn> findViewBookingReturnByUserClientIdAndBookingIdAndBookingStateId(
      @Param("userClientId") Integer userClientId, @Param("bookingId") Integer bookingId,
      @Param("bookingStateId") Integer bookingStateId);

  @Query("SELECT * FROM ViewBookingReturn WHERE userPromotorId = :userPromoterId AND bookingStateId = :bookingStateId")
  Flux<ViewBookingReturn> findAllViewBookingReturnByUsePromoterIdAndBookingStateId(
      @Param("userPromoterId") Integer userPromoterId,
      @Param("bookingStateId") Integer bookingStateId);

  @Query("SELECT * FROM ViewBookingReturn WHERE receptionistId = :receptionistId AND bookingStateId = :bookingStateId")
  Flux<ViewBookingReturn> findAllViewBookingReturnByReceptionistIdAndBookingStateId(
      @Param("receptionistId") Integer receptionistId,
      @Param("bookingStateId") Integer bookingStateId);

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

  @Query("SELECT us.firstname, us.lastname,us.documenttypeid,us.documentnumber,us.cellnumber, bo.bookingid, rt.roomtypeid, rt.roomtypename, rid.image, "
      +
      "r.offertimeinit, r.offertimeend, us.email, bo.costfinal, " +
      "TO_CHAR(bo.daybookinginit, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookinginit, " +
      "TO_CHAR(bo.daybookingend, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookingend, " +
      "bs.bookingstateid, bs.bookingstatename, bt.bedtypename, bt.bedtypedescription," +
      "SUM(bo.numberchildren+bo.numberbabies+bo.numberadultsextra+bo.numberadults+bo.numberadultsmayor) as capacity, "
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
      "bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd) " +
      "GROUP BY us.firstname, us.lastname, bo.bookingid, rt.roomtypeid, rt.roomtypename, rid.image, "
      +
      "r.offertimeinit, r.offertimeend, us.email, bo.costfinal, bo.daybookinginit, bo.daybookingend, "
      +
      "bs.bookingstateid, bs.bookingstatename, bt.bedtypename, bt.bedtypedescription, " +
      "r.riberapoints, r.inresortpoints, r.points,us.documenttypeid,us.documentNumber,us.cellnumber "
      +
      "ORDER BY bo.bookingid DESC " +
      "LIMIT :limit OFFSET :offset")
  Flux<BookingStates> findBookingsByStateIdPaginated(
      @Param("bookingStateId") Integer bookingStateId,
      @Param("roomTypeId") Integer roomTypeId,
      @Param("offertimeInit") LocalDateTime offertimeInit,
      @Param("offertimeEnd") LocalDateTime offertimeEnd,
      @Param("limit") int limit,
      @Param("offset") int offset);

  @Query("SELECT us.firstname, us.lastname, bo.bookingid, rt.roomtypeid, rt.roomtypename, rid.image, " +
      "r.offertimeinit, r.offertimeend, us.email, bo.costfinal, " +
      "TO_CHAR(bo.daybookinginit, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookinginit, " +
      "TO_CHAR(bo.daybookingend, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookingend, " +
      "bs.bookingstateid, bs.bookingstatename, bt.bedtypename, bt.bedtypedescription,bo.numberadults"
      +
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
      "AND (:offertimeInit IS NULL OR :offertimeEnd IS NULL OR " +
      "bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd) ")
  Mono<Long> countBookingsByStateId(
      @Param("bookingStateId") Integer bookingStateId,
      @Param("roomTypeId") Integer roomTypeId,
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

  @Query("""
      select b.*,r.roomname,COALESCE(r.roomdescription,r.roomname) as roomdescription from
      booking b
      join roomoffer ro on ro.roomofferid=b.roomofferid
      join room r on r.roomid=ro.roomid
              where b.bookingid=:bookingId
                              """)
  Mono<BookingAndRoomNameDto> getRoomNameAndDescriptionfindByBookingId(int bookinId);

  @Query("""
      SELECT
        b.bookingid,
        b.roomofferid,
        b.bookingstateid,
        b.userclientid,
        b.userpromotorid,
        b.costfinal,
        b.createdat as booking_createdat,
        p.totalcost,
        p.paymentbookid,
        p2.paymentmethodid,
        p2.description,
        p3.paymentsubtypeid,
        p3.paymentsubtypedesc,
        rt.roomtypename AS roomtypename,
        i.keysupplier,
        i.id AS invoice_id,
        i.serie,
        i.correlative,
        i.subtotal,
        i.identifierclient,
        i.createdat AS invoice_createdat,
        i.idtype,
        i.idcurrency,
        i.tc,
        i.totaligv,
        i.totalpayment,
        i.iduser,
        i.nameclient,
        u.username
      FROM
        booking b
      JOIN paymentbook p ON b.bookingid = p.bookingid AND p.pendingpay = 1
      JOIN invoice i ON p.paymentbookid = i.idpaymentbook
      join paymentmethod p2  on p.paymentmethodid = p2.paymentmethodid
      join paymentsubtype p3 on p.paymentsubtypeid = p3.paymentsubtypeid
      JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
      JOIN room r ON ro.roomid = r.roomid
      JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
      join userclient u on i.iduser = u.userclientid
      WHERE b.bookingstateid = :stateId
      AND (:month = 0 OR TO_CHAR(i.createdat, 'MM/YYYY') = CAST((:month) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE))
                                                                        """)
  Flux<BookingWithPaymentDTO> findBookingsWithPaymentByStateId(Integer stateId, Integer month);

  @Query("""
      SELECT
        b.bookingid,
        b.roomofferid,
        b.bookingstateid,
        b.userclientid,
        b.userpromotorid,
        b.costfinal,
        b.createdat as booking_createdat,
        p.totalcost,
        p.paymentbookid,
        p2.paymentmethodid,
        p2.description,
        p3.paymentsubtypeid,
        p3.paymentsubtypedesc,
        rt.roomtypename AS roomtypename,
        i.keysupplier,
        i.id AS invoice_id,
        i.serie,
        i.correlative,
        i.subtotal,
        i.identifierclient,
        i.createdat AS invoice_createdat,
        i.idtype,
        i.idcurrency,
        i.tc,
        i.totaligv,
        i.totalpayment,
        i.iduser,
        i.nameclient,
        u.username,
        u.documenttypeid
      FROM
        booking b
      JOIN paymentbook p ON b.bookingid = p.bookingid AND p.pendingpay = 1
      JOIN invoice i ON p.paymentbookid = i.idpaymentbook
      join paymentmethod p2  on p.paymentmethodid = p2.paymentmethodid
      join paymentsubtype p3 on p.paymentsubtypeid = p3.paymentsubtypeid
      JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
      JOIN room r ON ro.roomid = r.roomid
      JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
      join userclient u on i.iduser = u.userclientid
                              WHERE b.bookingstateid = :stateId
                              AND ((:dateini IS NULL AND :datefin IS NULL) OR (DATE(i.createdat) >= :dateini AND DATE(i.createdat) <= :datefin))
                                                                        """)
  Flux<BookingWithPaymentDTO> findBookingsWithPaymentByStateIdAndDate(Integer stateId, LocalDateTime dateini,
      LocalDateTime datefin);

  @Query("""
          SELECT *
          FROM booking
          WHERE roomofferid = :roomOfferId
            AND (daybookinginit < :offerTimeEnd AND daybookingend > :offerTimeInit)
            AND (bookingstateid != 4)
      """)
  Flux<BookingEntity> findConflictingBookings(@Param("roomOfferId") Integer roomOfferId,
      @Param("offerTimeInit") LocalDate offerTimeInit,
      @Param("offerTimeEnd") LocalDate offerTimeEnd);

  @Query("""
        SELECT COALESCE(trunc(sum(p.totalcost)::numeric, 2),0)
        FROM booking b
        JOIN paymentbook p ON b.bookingid = p.bookingid AND p.pendingpay = 1
        JOIN invoice i ON p.paymentbookid = i.idpaymentbook
        join paymentmethod p2  on p.paymentmethodid = p2.paymentmethodid
        join paymentsubtype p3 on p.paymentsubtypeid = p3.paymentsubtypeid
        JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
        JOIN room r ON ro.roomid = r.roomid
        JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
        join userclient u on i.iduser = u.userclientid
        WHERE b.bookingstateid = :stateId
        AND (TO_CHAR(i.createdat, 'MM/YYYY') = CAST((:month) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE))
      """)
  Mono<BigDecimal> getTotalSalesByMonth(Integer stateId, Integer month);

  @Query("""
        SELECT COALESCE(trunc(sum(p.totalcost)::numeric, 2),0)
        FROM booking b
        JOIN paymentbook p ON b.bookingid = p.bookingid AND p.pendingpay = 1
        JOIN invoice i ON p.paymentbookid = i.idpaymentbook
        join paymentmethod p2  on p.paymentmethodid = p2.paymentmethodid
        join paymentsubtype p3 on p.paymentsubtypeid = p3.paymentsubtypeid
        JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
        JOIN room r ON ro.roomid = r.roomid
        JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
        join userclient u on i.iduser = u.userclientid
        WHERE b.bookingstateid = :stateId
        AND (TO_CHAR(i.createdat, 'MM/YYYY') = CASE :month WHEN 1 THEN TO_CHAR(CURRENT_DATE + interval '-1 month', 'MM/YYYY')
        								  	ELSE CAST((:month-1) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE) END)
      """)
  Mono<BigDecimal> getTotalSalesBeforeMonth(Integer stateId, Integer month);

  @Query("""
        SELECT COUNT(*)
              FROM booking bo
              JOIN roomoffer r ON r.roomofferid = bo.roomofferid
              JOIN room rid ON rid.roomid = r.roomid
              JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid
              JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid
              JOIN userclient us ON us.userclientid = bo.userclientid
              JOIN bedroom be ON be.roomid = rid.roomid
              JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid
              WHERE bo.bookingstateid = 4
              AND (:month = 0 OR TO_CHAR(bo.createdat, 'MM/YYYY') = CAST((:month) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE))
      """)
  Mono<Long> getTotalCancellSales(Integer month);

  @Query("""
        SELECT COUNT(*)
              FROM booking bo
              JOIN roomoffer r ON r.roomofferid = bo.roomofferid
              JOIN room rid ON rid.roomid = r.roomid
              JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid
              JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid
              JOIN userclient us ON us.userclientid = bo.userclientid
              JOIN bedroom be ON be.roomid = rid.roomid
              JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid
              WHERE bo.bookingstateid = 4
              AND (TO_CHAR(bo.createdat, 'MM/YYYY') = CASE :month WHEN 1 THEN TO_CHAR(CURRENT_DATE + interval '-1 month', 'MM/YYYY')
              								  	ELSE CAST((:month-1) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE) END)
      """)
  Mono<Long> getTotalCancellLastSales(Integer month);

  @Query("""
      SELECT
      t.invoice_createdat,
      t.roomtypename,
      sum(t.totalcost) totalcost
         FROM (SELECT
                 b.bookingid,
                 b.roomofferid,
                 b.bookingstateid,
                 b.userclientid,
                 b.userpromotorid,
                 b.costfinal,
                 TO_CHAR(b.createdat, 'MM/YYYY') as booking_createdat,
                 p.totalcost,
                 p.paymentbookid,
                 p2.paymentmethodid,
                 p2.description,
                 p3.paymentsubtypeid,
                 p3.paymentsubtypedesc,
                 rt.roomtypename AS roomtypename,
                 i.keysupplier,
                 i.id AS invoice_id,
                 i.serie,
                 i.correlative,
                 i.subtotal,
                 i.identifierclient,
                 TO_CHAR(i.createdat, 'MM/YYYY') AS invoice_createdat,
                 i.idtype,
                 i.idcurrency,
                 i.tc,
                 i.totaligv,
                 i.totalpayment,
                 i.iduser,
                 i.nameclient,
                 u.username
              FROM booking b
                 JOIN paymentbook p ON b.bookingid = p.bookingid AND p.pendingpay = 1
                 JOIN invoice i ON p.paymentbookid = i.idpaymentbook
                 JOIN paymentmethod p2  on p.paymentmethodid = p2.paymentmethodid
                 JOIN paymentsubtype p3 on p.paymentsubtypeid = p3.paymentsubtypeid
                 JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
                 JOIN room r ON ro.roomid = r.roomid
                 JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
                 JOIN userclient u on i.iduser = u.userclientid
                 WHERE b.bookingstateid = :stateId
                 AND (:month = 0 OR TO_CHAR(i.createdat, 'MM/YYYY') = CAST((:month) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE))
      		) t
      	GROUP BY t.invoice_createdat, t.roomtypename
      	ORDER BY t.invoice_createdat
      """)
  Flux<BookingResumenPaymentDTO> findBookingsWithResumeByStateId(Integer stateId, Integer month);

  @Query("""
        SELECT
        	COALESCE(sum(p.totalcost), 0) totalcost
        FROM booking b
        JOIN paymentbook p ON b.bookingid = p.bookingid AND p.pendingpay = 1
        JOIN invoice i ON p.paymentbookid = i.idpaymentbook
        JOIN paymentmethod p2  on p.paymentmethodid = p2.paymentmethodid
        JOIN paymentsubtype p3 on p.paymentsubtypeid = p3.paymentsubtypeid
        JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
        JOIN room r ON ro.roomid = r.roomid
        JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
        JOIN userclient u on i.iduser = u.userclientid
        WHERE b.bookingstateid = 2
        AND EXTRACT(YEAR FROM i.createdat) = EXTRACT(YEAR FROM CURRENT_DATE - INTERVAL '1 years')
      """)
  Mono<BigDecimal> getTotalBeforeYear();

  @Query("""
        SELECT
          count(*) countClient
        FROM booking b
        JOIN paymentbook p ON b.bookingid = p.bookingid AND p.pendingpay = 1
        JOIN invoice i ON p.paymentbookid = i.idpaymentbook
        JOIN paymentmethod p2  on p.paymentmethodid = p2.paymentmethodid
        JOIN paymentsubtype p3 on p.paymentsubtypeid = p3.paymentsubtypeid
        JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
        JOIN room r ON ro.roomid = r.roomid
        JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
        JOIN userclient u on i.iduser = u.userclientid
        WHERE (:stateId IS NULL OR b.bookingstateid = :stateId)
        AND (:month = 0 OR TO_CHAR(b.createdat, 'MM/YYYY') = CAST((:month) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE))
      """)
  Mono<Long> getTotalActiveClients(Integer stateId, Integer month);

  @Query("""
        SELECT
          count(*) countClient
        FROM booking b
        JOIN paymentbook p ON b.bookingid = p.bookingid AND p.pendingpay = 1
        JOIN invoice i ON p.paymentbookid = i.idpaymentbook
        JOIN paymentmethod p2  on p.paymentmethodid = p2.paymentmethodid
        JOIN paymentsubtype p3 on p.paymentsubtypeid = p3.paymentsubtypeid
        JOIN roomoffer ro ON b.roomofferid = ro.roomofferid
        JOIN room r ON ro.roomid = r.roomid
        JOIN roomtype rt ON r.roomtypeid = rt.roomtypeid
        JOIN userclient u on i.iduser = u.userclientid
        WHERE (:stateId IS NULL OR b.bookingstateid = :stateId)
        AND (TO_CHAR(b.createdat, 'MM/YYYY') = CASE :month WHEN 1 THEN TO_CHAR(CURRENT_DATE + interval '-1 month', 'MM/YYYY')
                                              ELSE CAST((:month-1) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE) END)
      """)
  Mono<Long> getTotalActiveClientsMonths(Integer stateId, Integer month);

  @Query("SELECT SUM(b.costFinal) FROM booking b WHERE b.userpromotorid = :userPromoterId AND b.bookingStateId = :bookingStateId")
  Mono<BigDecimal> findTotalAmountByUserPromoterIdAndBookingStateId(@Param("userPromoterId") Integer userPromoterId,
      @Param("bookingStateId") Integer bookingStateId);

  Flux<BookingEntity> findByUserPromotorIdAndBookingStateId(Integer userPromotorId, Integer bookingStateId);

}