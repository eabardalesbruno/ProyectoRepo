package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingResumenPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingWithPaymentDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.admin.dto.booking.response.BookingDetailDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.*;
import com.proriberaapp.ribera.Api.controllers.client.dto.response.BookingConflictDto;
import com.proriberaapp.ribera.Domain.dto.BookingAndRoomNameDto;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.BookingFeedingEntity;
import com.proriberaapp.ribera.utils.emails.BookingEmailDto;
import com.proriberaapp.ribera.utils.emails.bookingRejectUserEmailDto;

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
  /*
  @Query("SELECT * FROM booking WHERE roomofferid = :roomOfferId AND ((daybookinginit <= :dayBookingEnd) AND (daybookingend >= :dayBookingInit)) AND bookingStateId != 4")
  Flux<BookingEntity> findExistingBookings(@Param("roomOfferId") Integer roomOfferId,
      @Param("dayBookingInit") Timestamp dayBookingInit,
      @Param("dayBookingEnd") Timestamp dayBookingEnd);
  */
  @Query(value = """
          SELECT *
          FROM booking
          WHERE roomofferid = :roomOfferId
            AND userclientid = :userClientId
            AND (
              (daybookinginit <= :dayBookingEnd)
              AND (daybookingend >= :dayBookingInit)
            )
            AND bookingStateId != 4;
          """)
  Flux<BookingEntity> findExistingBookings(@Param("roomOfferId") Integer roomOfferId,
                                           @Param("userClientId") Integer userClientId,
                                           @Param("dayBookingInit") Timestamp dayBookingInit,
                                           @Param("dayBookingEnd") Timestamp dayBookingEnd);

  @Query("SELECT bookingid FROM booking WHERE bookingid = :bookingId AND userclientid != :userId AND ((daybookinginit <= :dayBookingEnd) AND (daybookingend >= :dayBookingInit)) AND bookingStateId != 4")
  Flux<BookingEntity> findExistingBookingsAndUserClientId(@Param("bookingId") Integer bookingId,
      @Param("userId") Integer userId,
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

  @Query("SELECT * FROM ViewBookingReturn WHERE receptionistid = :receptionistId AND bookingStateId = :bookingStateId")
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

  @Query("""

          SELECT distinct us.firstname, us.lastname,us.documenttypeid,us.documentnumber,us.cellnumber, bo.bookingid, rt.roomtypeid, rt.roomtypename, rid.image,
                 r.offertimeinit, r.offertimeend, us.email, bo.costfinal,bo.createdat,
                 TO_CHAR(bo.daybookinginit, 'YYYY-MM-DD HH24:MI:SS') AS daybookinginit,
                 TO_CHAR(bo.daybookingend, 'YYYY-MM-DD HH24:MI:SS') AS daybookingend,
                     bs.bookingstateid, bs.bookingstatename,
                         rid.roomnumber,
      		   (SELECT b.bedtypename FROM bedroom be
      		   INNER JOIN bedstype b ON b.bedtypeid = be.bedtypeid
      		   WHERE be.roomid = rid.roomid LIMIT 1) bedtypename,
      		   (SELECT b.bedtypedescription FROM bedroom be
      		   INNER JOIN bedstype b ON b.bedtypeid = be.bedtypeid
      		   WHERE be.roomid = rid.roomid LIMIT 1) bedtypename,
                 bo.numberchildren+bo.numberbabies+bo.numberadultsextra+bo.numberadults+bo.numberadultsmayor as capacity,
                     r.riberapoints, r.inresortpoints, r.points,
             (CASE
                WHEN up.userpromoterid is not null  THEN concat('PROMOTOR ',' - ',up.firstname,' ',up.lastname)
                WHEN ua.useradminid is not null THEN concat('RECEPCION',' - ',ua.firstname,' ',ua.lastname)
                when us.userclientid is not null and us.isuserinclub THEN 'WEB - Socio'
                when us.userclientid is not null and not us.isuserinclub THEN 'WEB'
                  ELSE 'Sin clasificar' END) as channel,
                pb.paymentbookid, (select case  when bookingfeedingid is not null then true else false end from booking_feeding bf where bf.bookingid=bo.bookingid limit 1) as isalimentation,
                i.serie,
                i.linkpdf,
                i.operationcode,
                LPAD(CAST(i.correlative AS CHAR),9,'0') operationnumber,
                (SELECT pm.description FROM paymentmethod pm WHERE pm.paymentmethodid = pb.paymentmethodid) methods
               FROM userclient us
      		   JOIN booking bo ON us.userclientid = bo.userclientid
                 JOIN roomoffer r ON r.roomofferid = bo.roomofferid
                 JOIN room rid ON rid.roomid = r.roomid
                 JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid
                  JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid
                  left join userpromoter up on up.userpromoterid=bo.userpromotorid
                  left join useradmin ua on ua.useradminid=bo.receptionistid
                  left join paymentbook pb on pb.bookingid=bo.bookingid
		          left join invoice i on i.idpaymentbook = pb.paymentbookid
                  WHERE bo.bookingstateid in(:bookingStateId)
                 AND (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId)
                 AND (:offertimeInit IS NULL OR :offertimeEnd IS NULL OR
                 bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd)
                ORDER BY bo.bookingid DESC
                 LIMIT :limit OFFSET :offset
      """)
  Flux<BookingStates> findBookingsByStateIdPaginated(
      @Param("bookingStateId") List<Integer> bookingStateId,
      @Param("roomTypeId") Integer roomTypeId,
      @Param("offertimeInit") LocalDateTime offertimeInit,
      @Param("offertimeEnd") LocalDateTime offertimeEnd,
      @Param("limit") int limit,
      @Param("offset") int offset);

  @Query("""

          SELECT distinct us.firstname, us.lastname,us.documenttypeid,us.documentnumber,us.cellnumber, bo.bookingid, rt.roomtypeid, rt.roomtypename, rid.image,
                 r.offertimeinit, r.offertimeend, us.email, bo.costfinal,
                 TO_CHAR(bo.daybookinginit, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookinginit,
                 TO_CHAR(bo.daybookingend, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookingend,
                     bs.bookingstateid, bs.bookingstatename,
                         rid.roomnumber,
      		   (SELECT b.bedtypename FROM bedroom be
      		   INNER JOIN bedstype b ON b.bedtypeid = be.bedtypeid
      		   WHERE be.roomid = rid.roomid LIMIT 1) bedtypename,
      		   (SELECT b.bedtypedescription FROM bedroom be
      		   INNER JOIN bedstype b ON b.bedtypeid = be.bedtypeid
      		   WHERE be.roomid = rid.roomid LIMIT 1) bedtypename,
                 bo.numberchildren+bo.numberbabies+bo.numberadultsextra+bo.numberadults+bo.numberadultsmayor as capacity,
                     r.riberapoints, r.inresortpoints, r.points,
             (CASE
                WHEN up.userpromoterid is not null  THEN concat('PROMOTOR ',' - ',up.firstname,' ',up.lastname)
                WHEN ua.useradminid is not null THEN concat('RECEPCION',' - ',ua.firstname,' ',ua.lastname)
                when us.userclientid is not null and us.isuserinclub THEN 'WEB - Socio'
                when us.userclientid is not null and not us.isuserinclub THEN 'WEB'
                  ELSE 'Sin clasificar' END) as channel,
                  pb.paymentbookid, (select case  when bookingfeedingid is not null then true else false end from booking_feeding bf where bf.bookingid=bo.bookingid limit 1) as isalimentation
                 FROM userclient us
      		   JOIN booking bo ON us.userclientid = bo.userclientid
                 JOIN roomoffer r ON r.roomofferid = bo.roomofferid
                 JOIN room rid ON rid.roomid = r.roomid
                 JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid
                  JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid
                  left join userpromoter up on up.userpromoterid=bo.userpromotorid
                  left join useradmin ua on ua.useradminid=bo.receptionistid
                          left join paymentbook pb on pb.bookingid=bo.bookingid
                  WHERE bo.bookingstateid = :bookingStateId
                 AND (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId)
                 AND (:offertimeInit IS NULL OR :offertimeEnd IS NULL OR
                 bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd)
                ORDER BY bo.bookingid DESC
                 LIMIT :limit OFFSET :offset
      """)
  Flux<BookingStates> findBookingsByStateIdPaginated(
      @Param("bookingStateId") Integer bookingStateId,
      @Param("roomTypeId") Integer roomTypeId,
      @Param("offertimeInit") LocalDateTime offertimeInit,
      @Param("offertimeEnd") LocalDateTime offertimeEnd,
      @Param("limit") int limit,
      @Param("offset") int offset);

  @Query("SELECT * FROM ViewBookingReturn WHERE numberAdults = :numberAdults AND numberChildren = :numberChildren AND numberBabies = :numberBabies AND userClientId = :userClientId AND bookingStateId = :bookingStateId")
  Flux<ViewBookingReturn> findAllViewBookingReturnByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(
      @Param("numberAdults") Integer numberAdults, @Param("numberChildren") Integer numberChildren,
      @Param("numberBabies") Integer numberBabies, @Param("userClientId") Integer userClientId,
      @Param("bookingStateId") Integer bookingStateId);
  /*
  @Query("SELECT us.firstname, us.lastname, bo.bookingid, rt.roomtypeid, rt.roomtypename, rid.image, " +
      "r.offertimeinit, r.offertimeend, us.email, bo.costfinal, " +
      "TO_CHAR(bo.daybookinginit, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookinginit, " +
      "TO_CHAR(bo.daybookingend, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS daybookingend, " +
      "bs.bookingstateid, bs.bookingstatename, bt.bedtypename, bt.bedtypedescription,bo.numberadults"
      +
      ",bo.numberchildren,bo.numberbabies,bo.numberadultsextra,bo.numberadultsmayor, "
      +
      "r.riberapoints, r.inresortpoints, bo.total_rewards as points, " +
      "calculate_nights(bo.daybookinginit,bo.daybookingend ) as nights ,bo.createdat " +
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
      @Param("offset") int offset, @Param("userId") Integer userId);
  */
  @Query(value = """
          SELECT
              us.firstname,
              us.lastname,
              bo.bookingid,
              rt.roomtypeid,
              rt.roomtypename,
              rid.image,
              r.offertimeinit,
              r.offertimeend,
              us.email,
              bo.costfinal,
              TO_CHAR(bo.daybookinginit, 'YYYY-MM-DD"T"HH24:MI:SS') AS daybookinginit,
              TO_CHAR(bo.daybookingend, 'YYYY-MM-DD"T"HH24:MI:SS') AS daybookingend,
              bs.bookingstateid,
              bs.bookingstatename,
              STRING_AGG(CONCAT(be.quantity, ' ', bt.bedtypename), ' + ' ORDER BY bt.bedtypename) AS bedtypename,
              bo.numberadults,
              bo.numberchildren,
              bo.numberbabies,
              bo.numberadultsextra,
              bo.numberadultsmayor,
              r.riberapoints,
              r.inresortpoints,
              bo.total_rewards AS points,
              calculate_nights(bo.daybookinginit, bo.daybookingend) AS nights,
              bo.createdat
          FROM
              booking bo
          JOIN
              roomoffer r ON r.roomofferid = bo.roomofferid
          JOIN
              room rid ON rid.roomid = r.roomid
          JOIN
              roomtype rt ON rt.roomtypeid = rid.roomtypeid
          JOIN
              bookingstate bs ON bo.bookingstateid = bs.bookingstateid
          JOIN
              userclient us ON us.userclientid = bo.userclientid
          LEFT JOIN
              bedroom be ON be.roomid = rid.roomid -- LEFT JOIN para incluir habitaciones sin configuración de cama
          LEFT JOIN
              bedstype bt ON bt.bedtypeid = be.bedtypeid
          WHERE
              bo.bookingstateid = :bookingStateId
              AND (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId)
              AND (
                  :offertimeInit IS NULL
                  OR :offertimeEnd IS NULL
                  OR (
                      bo.daybookinginit >= :offertimeInit
                      AND bo.daybookingend <= :offertimeEnd
                  )
              )
              AND bo.userclientid = :userId
          GROUP BY
              us.firstname,
              us.lastname,
              bo.bookingid,
              rt.roomtypeid,
              rt.roomtypename,
              rid.image,
              r.offertimeinit,
              r.offertimeend,
              us.email,
              bo.costfinal,
              daybookinginit,
              daybookingend,
              bs.bookingstateid,
              bs.bookingstatename,
              bo.numberadults,
              bo.numberchildren,
              bo.numberbabies,
              bo.numberadultsextra,
              bo.numberadultsmayor,
              r.riberapoints,
              r.inresortpoints,
              bo.total_rewards,
              nights,
              bo.createdat
          ORDER BY
              bo.bookingid DESC
          LIMIT
              :limit OFFSET :offset;
          """)
  Flux<BookingStates> findBookingsByStateIdPaginatedAndUserId(
          @Param("bookingStateId") Integer bookingStateId,
          @Param("roomTypeId") Integer roomTypeId,
          @Param("offertimeInit") LocalDateTime offertimeInit,
          @Param("offertimeEnd") LocalDateTime offertimeEnd,
          @Param("limit") int limit,
          @Param("offset") int offset, @Param("userId") Integer userId);

  @Query("SELECT count(*) " +
      "FROM booking bo " +
      "JOIN roomoffer r ON r.roomofferid = bo.roomofferid " +
      "JOIN room rid ON rid.roomid = r.roomid " +
      "JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid " +
      "JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid " +
      "JOIN userclient us ON us.userclientid = bo.userclientid " +
      "JOIN bedroom be ON be.roomid = rid.roomid " +
      "JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid " +
      "WHERE bo.bookingstateid  in(:bookingStateId) " +
      "AND (:roomTypeId IS NULL OR rt.roomtypeid = :roomTypeId) " +
      "AND (:offertimeInit IS NULL OR :offertimeEnd IS NULL OR " +
      "bo.daybookinginit >= :offertimeInit AND bo.daybookingend <= :offertimeEnd) ")
  Mono<Long> countBookingsByStateId(
      @Param("bookingStateId") List<Integer> bookingStateId,
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
      @Param("offertimeEnd") LocalDateTime offertimeEnd, @Param("userId") Integer userId);

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
        u.username,
        p.totaldiscount,
        p.percentagediscount
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
      AND (0 = :month AND TO_CHAR(i.createdat, 'YYYY') = CAST((:year) AS VARCHAR))
      OR (0 < :month AND TO_CHAR(i.createdat, 'MM/YYYY') = LPAD(CAST((:month) AS VARCHAR), 2, '0') ||'/'|| CAST((:year) AS VARCHAR))
      """)
  Flux<BookingWithPaymentDTO> findBookingsWithPaymentByStateId(Integer stateId, Integer month, Integer year);

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

  /*
   * @Query("""
   * SELECT *
   * FROM booking
   * WHERE roomofferid = :roomOfferId
   * AND (daybookinginit < :offerTimeEnd AND daybookingend > :offerTimeInit)
   * AND (bookingstateid != 4)
   * """)
   */
  @Query("""
          SELECT *
          FROM booking
          WHERE roomofferid = :roomOfferId
            AND (daybookinginit < :offerTimeEnd AND daybookingend > :offerTimeInit)
            AND (bookingstateid in (2,7,6,3))
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
        AND (TO_CHAR(i.createdat, 'MM/YYYY') = LPAD(CAST((:month) AS VARCHAR), 2, '0') ||'/'|| CAST((:year) AS VARCHAR))
      """)
  Mono<BigDecimal> getTotalSalesByMonth(Integer stateId, Integer month, Integer year);

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
        								  	ELSE LPAD(CAST((:month-1) AS VARCHAR), 2, '0')  ||'/'|| CAST((:year) AS VARCHAR) END)
      """)
  Mono<BigDecimal> getTotalSalesBeforeMonth(Integer stateId, Integer month, Integer year);

  @Query("""
        SELECT COUNT(*)
       FROM (SELECT bo.bookingstateid
              FROM booking bo
              JOIN roomoffer r ON r.roomofferid = bo.roomofferid
              JOIN room rid ON rid.roomid = r.roomid
              JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid
              JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid
              JOIN userclient us ON us.userclientid = bo.userclientid
              JOIN bedroom be ON be.roomid = rid.roomid
              JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid
              WHERE (0 = :month AND TO_CHAR(bo.createdat, 'YYYY') = CAST((:year) AS VARCHAR))
           	  OR (0 < :month AND TO_CHAR(bo.createdat, 'MM/YYYY') = LPAD(CAST((:month) AS VARCHAR), 2, '0') ||'/'|| CAST((:year) AS VARCHAR))
       ) t WHERE t.bookingstateid = 4
      """)
  Mono<Long> getTotalCancellSales(Integer month, Integer year);

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
              								  	ELSE LPAD(CAST((:month-1) AS VARCHAR), 2, '0') ||'/'|| CAST((:year) AS VARCHAR) END)
      """)
  Mono<Long> getTotalCancellLastSales(Integer month, Integer year);

  @Query(value = """
           SELECT COUNT(*)
           FROM (SELECT bo.bookingstateid
              FROM booking bo
              JOIN roomoffer r ON r.roomofferid = bo.roomofferid
              JOIN room rid ON rid.roomid = r.roomid
              JOIN roomtype rt ON rt.roomtypeid = rid.roomtypeid
              JOIN bookingstate bs ON bo.bookingstateid = bs.bookingstateid
              JOIN userclient us ON us.userclientid = bo.userclientid
              JOIN bedroom be ON be.roomid = rid.roomid
              JOIN bedstype bt ON bt.bedtypeid = be.bedtypeid
              WHERE (0 = :month AND TO_CHAR(bo.createdat, 'YYYY') = CAST((:year) AS VARCHAR))
           	  OR (0 < :month AND TO_CHAR(bo.createdat, 'MM/YYYY') = LPAD(CAST((:month) AS VARCHAR), 2, '0') ||'/'|| CAST((:year) AS VARCHAR))
           ) t WHERE t.bookingstateid = 2
          """)
  Mono<Long> getCountTotalReservationsForMonthAndYear(Integer month, Integer year);

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
              WHERE bo.bookingstateid = 2
              AND (TO_CHAR(bo.createdat, 'MM/YYYY') = CASE :month WHEN 1 THEN TO_CHAR(CURRENT_DATE + interval '-1 month', 'MM/YYYY')
              								  	ELSE LPAD(CAST((:month-1) AS VARCHAR), 2, '0') ||'/'|| CAST((:year) AS VARCHAR) END)
      """)
  Mono<Long> getCountTotalReservationsForMonthAndYearLast(Integer month, Integer year);

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
                 AND (0 = :month AND TO_CHAR(i.createdat, 'YYYY') = CAST((:year) AS VARCHAR))
           		 OR (0 < :month AND TO_CHAR(i.createdat, 'MM/YYYY') = LPAD(CAST((:month) AS VARCHAR), 2, '0') ||'/'|| CAST((:year) AS VARCHAR))
      		) t
      	GROUP BY t.invoice_createdat, t.roomtypename
      	ORDER BY t.invoice_createdat
      """)
  Flux<BookingResumenPaymentDTO> findBookingsWithResumeByStateId(Integer stateId, Integer month, Integer year);

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
  Mono<BigDecimal> findTotalAmountByUserPromoterIdAndBookingStateId(
      @Param("userPromoterId") Integer userPromoterId,
      @Param("bookingStateId") Integer bookingStateId);

  Flux<BookingEntity> findByUserPromotorIdAndBookingStateId(Integer userPromotorId, Integer bookingStateId);

  @Query("""
        update booking set bookingstateid = :stateId where bookingid = :bookingId
      """)
  Mono<Void> updateState(Integer stateId, Integer bookingId);

  @Query("""
        update booking set bookingstateid = 4 where bookingid = :bookingId and  bookingstateid in(3,4)
      """)
  Mono<Void> updateStateToAnulated(Integer bookingId);

  @Query("SELECT * FROM booking WHERE userclientid = :userClientId")
  Mono<BookingEntity> findByUserClientId(@Param("userClientId") Integer userClientId);

  @Query("SELECT DISTINCT EXTRACT('Year' FROM createdat) years FROM invoice ORDER BY years")
  Flux<Long> getAllYearsInvoice();

  @Query("""
          select bo.*,r.roomname,u.email as clientemail,u.firstname as clientname
          from booking bo
          join roomoffer ro on ro.roomofferid = bo.roomofferid
          join room r on r.roomid = ro.roomid
          join userclient u on u.userclientid = bo.userclientid
          where bo.bookingid = :bookingId
      """)
  Mono<bookingRejectUserEmailDto> findBookingEmailDtoByBookingId(Integer bookingId);

  @Query("""
      select b.roomnumber,b.bookingid,b.roomname,b.numberinfants,to_char(b.daybookinginit,'DD/MM/YYYY HH24:MI') as checkin,to_char(b.daybookingend,'DD/MM/YYYY HH24:MI') as checkout,b.isalimentation,b.numberadultsmayor,b.numberadultsextra,b.numberadults,b.numberchildren,b.totalperson,(case when b.isalimentation then 	calculate_total_dinner( b.daybookingend,b.totalperson) else 0 end) as totaldinner,
          Case when b.isalimentation then b.totalperson  else 0 end as totallunch,b.totalperson as totalbreakfast	 from (
           select  b.bookingid,r.roomname,r.roomdescription,b.daybookinginit,b.daybookingend, COALESCE(b.numberadults,0) as numberadults ,COALESCE(b.numberchildren,0) as numberchildren,COALESCE(b.numberadultsextra,0) as numberadultsextra,
          	COALESCE(b.numberadultsmayor,0) as numberadultsmayor, COALESCE(b.numberbabies,0) as numberinfants,b.numberadults+b.numberchildren+b.numberadultsextra+b.numberadultsmayor as totalperson,
                          COALESCE((select case  when bf.bookingfeedingid is not null then true else null end from booking_feeding bf where bf.bookingid=b.bookingid limit 1),false) as isalimentation,
               (b.daybookingend::date - b.daybookinginit::date)+1 as total_days,
          	calculate_nights(b.daybookinginit, b.daybookingend) as total_nights,
          r.roomnumber
          	from paymentbook pb
                          join booking b on b.bookingid=pb.bookingid
                          join bookingstate bs on bs.bookingstateid=b.bookingstateid
           join roomoffer ro on ro.roomofferid=b.roomofferid
           join room r on r.roomid=ro.roomid
                          where bs.bookingstatename='OCUPADO'
                          and CURRENT_TIMESTAMP BETWEEN b.daybookinginit and b.daybookingend
           ) b
                         """)
  Flux<ReportOfKitchenBdDto> getReportOfKitchenBdDto();

  @Query("""
           select pm.description from paymentbook pb join paymentmethod pm on pb.paymentmethodid = pm.paymentmethodid
           where pb.bookingid = :bookingid
      """)
  Mono<String> fingMethodPäymentByBookingId(@Param("bookingid") Integer bookingid);

  @Query("""
        select * from booking_feeding bf where bf.bookingid=:bookingId
      """)
  Flux<BookingFeedingEntity> getSelectBookingFeedingOfBookingId(Integer bookingId);

  @Query("""
        select
           'INGRESO ' || TO_CHAR(b.daybookinginit, 'DD') || ' ' ||
             CASE EXTRACT(MONTH FROM b.daybookinginit)
               WHEN 1 THEN 'ENERO'
               WHEN 2 THEN 'FEBRERO'
               WHEN 3 THEN 'MARZO'
               WHEN 4 THEN 'ABRIL'
               WHEN 5 THEN 'MAYO'
               WHEN 6 THEN 'JUNIO'
               WHEN 7 THEN 'JULIO'
               WHEN 8 THEN 'AGOSTO'
               WHEN 9 THEN 'SEPTIEMBRE'
               WHEN 10 THEN 'OCTUBRE'
               WHEN 11 THEN 'NOVIEMBRE'
               WHEN 12 THEN 'DICIEMBRE'
             END AS checkin,
           'SALIDA ' || TO_CHAR(b.daybookingend, 'DD') || ' ' ||
             CASE EXTRACT(MONTH FROM b.daybookingend)
               WHEN 1 THEN 'ENERO'
               WHEN 2 THEN 'FEBRERO'
               WHEN 3 THEN 'MARZO'
               WHEN 4 THEN 'ABRIL'
               WHEN 5 THEN 'MAYO'
               WHEN 6 THEN 'JUNIO'
               WHEN 7 THEN 'JULIO'
               WHEN 8 THEN 'AGOSTO'
               WHEN 9 THEN 'SEPTIEMBRE'
               WHEN 10 THEN 'OCTUBRE'
               WHEN 11 THEN 'NOVIEMBRE'
               WHEN 12 THEN 'DICIEMBRE'
             END AS checkout
           from booking b
           where b.bookingid = :bookingId
    """)
  Mono<DetailBookInvoiceDto> getDetailBookInvoice(Integer bookingId);

  @Query(value = """
          update booking set costfinal = :newCostFinal where bookingid = :bookingId 
          """)
  Mono<Void> updateCostFinalByBookingId(Integer bookingId,BigDecimal newCostFinal);

  @Query(value = """
          SELECT
              b.bookingid AS bookingid,
              TO_CHAR(b.daybookinginit, 'DD/MM/YYYY') AS datebookinginit,
              TO_CHAR(b.daybookingend, 'DD/MM/YYYY') AS datebookingend
          FROM booking b
          WHERE b.roomofferid = :roomOfferId
            AND (:startDate::timestamp, :endDate::timestamp) OVERLAPS (b.daybookinginit, b.daybookingend)
            AND b.dayBookingEnd >= CURRENT_DATE
            AND b.bookingstateid != 4;
          """)
  Flux<BookingConflictDto> findNewConflictingBookings(Integer roomOfferId, String startDate, String endDate);

  @Query(value = """
          SELECT
              b.bookingid,
              b.costfinal,
              b.daybookinginit,
              b.daybookingend,
              b.roomofferid,
              r.roomnumber,
              r.roomname,
              r.image AS imgurl,
              bs.bookingstatename AS bookingstate,
              CONCAT(
                  CASE EXTRACT(DOW FROM b.daybookinginit)
                      WHEN 0 THEN 'Dom'
                      WHEN 1 THEN 'Lun'
                      WHEN 2 THEN 'Mar'
                      WHEN 3 THEN 'Mié'
                      WHEN 4 THEN 'Jue'
                      WHEN 5 THEN 'Vie'
                      WHEN 6 THEN 'Sáb'
                  END,
                  ', ',
                  EXTRACT(DAY FROM b.daybookinginit),
                  ' ',
                  CASE EXTRACT(MONTH FROM b.daybookinginit)
                      WHEN 1  THEN 'Ene'
                      WHEN 2  THEN 'Feb'
                      WHEN 3  THEN 'Mar'
                      WHEN 4  THEN 'Abr'
                      WHEN 5  THEN 'May'
                      WHEN 6  THEN 'Jun'
                      WHEN 7  THEN 'Jul'
                      WHEN 8  THEN 'Ago'
                      WHEN 9  THEN 'Set'
                      WHEN 10 THEN 'Oct'
                      WHEN 11 THEN 'Nov'
                      WHEN 12 THEN 'Dic'
                  END
              ) AS checkin,
              CONCAT(
                  CASE EXTRACT(DOW FROM b.daybookingend)
                      WHEN 0 THEN 'Dom'
                      WHEN 1 THEN 'Lun'
                      WHEN 2 THEN 'Mar'
                      WHEN 3 THEN 'Mié'
                      WHEN 4 THEN 'Jue'
                      WHEN 5 THEN 'Vie'
                      WHEN 6 THEN 'Sáb'
                  END,
                  ', ',
                  EXTRACT(DAY FROM b.daybookingend),
                  ' ',
                  CASE EXTRACT(MONTH FROM b.daybookingend)
                      WHEN 1  THEN 'Ene'
                      WHEN 2  THEN 'Feb'
                      WHEN 3  THEN 'Mar'
                      WHEN 4  THEN 'Abr'
                      WHEN 5  THEN 'May'
                      WHEN 6  THEN 'Jun'
                      WHEN 7  THEN 'Jul'
                      WHEN 8  THEN 'Ago'
                      WHEN 9  THEN 'Set'
                      WHEN 10 THEN 'Oct'
                      WHEN 11 THEN 'Nov'
                      WHEN 12 THEN 'Dic'
                  END
              ) AS checkout,
              CASE
                  WHEN (b.daybookingend::DATE - b.daybookinginit::DATE) = 1 THEN '1 noche'
                  ELSE (b.daybookingend::DATE - b.daybookinginit::DATE)::TEXT || ' noches'
              END AS totalnights,
              CONCAT(
                  CASE
                      WHEN b.numberadults > 0 THEN
                          b.numberadults || ' ' ||
                          CASE WHEN b.numberadults = 1 THEN 'adulto' ELSE 'adultos' END
                      ELSE NULL
                  END,
                  CASE
                      WHEN (b.numberadults > 0 AND (b.numberchildren > 0 OR b.numberbabies > 0)) THEN ', '
                      ELSE ''
                  END,
                  CASE
                      WHEN b.numberchildren > 0 THEN
                          b.numberchildren || ' ' ||
                          CASE WHEN b.numberchildren = 1 THEN 'niño' ELSE 'niños' END
                      ELSE NULL
                  END,
                  CASE
                      WHEN (b.numberchildren > 0 AND b.numberbabies > 0) THEN ', '
                      ELSE ''
                  END,
                  CASE
                      WHEN b.numberbabies > 0 THEN
                          b.numberbabies || ' ' ||
                          CASE WHEN b.numberbabies = 1 THEN 'bebé' ELSE 'bebés' END
                      ELSE NULL
                  END
              ) AS totalpeople,
              b.userclientid
          FROM booking b
          INNER JOIN bookingstate bs ON bs.bookingstateid = b.bookingstateid
          INNER JOIN roomoffer ro ON ro.roomofferid = b.roomofferid
          INNER JOIN room r ON r.roomid = ro.roomid
          WHERE b.bookingid = :bookingId;
          """)
  Mono<BookingDetailDto> findBookingDetailByBookingId(Integer bookingId);
}