package com.proriberaapp.ribera.Infraestructure.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.r2dbc.core.DatabaseClient;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.dto.activity.GuestInfoDTO;
import com.proriberaapp.ribera.Domain.dto.activity.PaymentInfoDTO;
import com.proriberaapp.ribera.Domain.dto.activity.ReservationDetailDTO;
import com.proriberaapp.ribera.Domain.dto.activity.RoomCapacityDTO;
import com.proriberaapp.ribera.Domain.dto.activity.RoomDetailDTO;
import com.proriberaapp.ribera.Domain.dto.activity.StandbyInfoDTO;
import com.proriberaapp.ribera.Infraestructure.repository.activity.ActivityDashboardCustomRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ActivityDashboardCustomRepositoryImpl implements ActivityDashboardCustomRepository {
    private final DatabaseClient databaseClient;

    public ActivityDashboardCustomRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<RoomDetailDTO> findAllRoomsPaginated(LocalDateTime dateStart, LocalDateTime dateEnd) {
        String sql = """
                 SELECT
                    r.roomid,
                    r.roomnumber,
                    r.roomname,
                    rt.roomtypename,
                    b.bookingid,
                    b.daybookinginit,
                    b.daybookingend,
                    b.numberadults,
                    b.numberchildren,
                    b.numberbabies,
                    b.numberadultsextra,
                    b.numberadultsmayor,
                    b.bookingstateid,
                    uc.firstname,
                    uc.lastname,
                    uc.isuserinclub,
                    CASE uc.isuserinclub 
                        WHEN true THEN 'Socio'
                        ELSE 'Externo'
                    END as client_type,
                    CASE 
                        WHEN b.daybookingend IS NOT NULL AND b.daybookinginit IS NOT NULL 
                        THEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer 
                        ELSE 0 
                    END as total_nights,
                    (b.numberadults + b.numberadultsextra + b.numberadultsmayor) as total_adults,
                    (b.numberchildren + b.numberbabies) as total_children,
                    CASE 
                        WHEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer >= 30 THEN '30 dias'
                        WHEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer >= 21 THEN '21 dias'
                        WHEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer >= 14 THEN '14 dias'
                        WHEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer >= 7 THEN '7 dias'
                        ELSE '48 horas'
                    END as reservation_time,
                    CASE 
                        WHEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer >= 30 AND uc.isuserinclub = true THEN '24 horas'
                        WHEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer >= 21 AND uc.isuserinclub = true THEN '18 horas'
                        WHEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer >= 14 THEN '12 horas'
                        WHEN (DATE(b.daybookingend) - DATE(b.daybookinginit))::integer >= 7 AND uc.isuserinclub = true THEN '6 horas'
                        ELSE '2 horas'
                    END as standby_time,
                    CASE
                        WHEN b.bookingid IS NULL THEN 'DISPONIBLE'
                        WHEN b.bookingstateid = 1 THEN 'NO SHOW'
                        WHEN b.bookingstateid = 3 THEN 'RESERVADO'
                        WHEN pb.paymentstateid = 2 THEN 'PAGADO'
                        ELSE 'OTRO'
                    END as status,
                    pb.paymentstateid,
                    pm.description as payment_method,
                    bf.bookingfeedingid IS NOT NULL as has_feeding
                FROM room r
                LEFT JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
                LEFT JOIN roomoffer ro ON ro.roomid = r.roomid
                LEFT JOIN booking b ON b.roomofferid = ro.roomofferid
                    AND DATE(b.daybookinginit) <= :dateStart
                    AND DATE(b.daybookingend) >= :dateEnd
                LEFT JOIN userclient uc ON uc.userclientid = b.userclientid
                LEFT JOIN paymentbook pb ON pb.bookingid = b.bookingid
                LEFT JOIN paymentmethod pm ON pm.paymentmethodid = pb.paymentmethodid
                LEFT JOIN booking_feeding bf ON bf.bookingid = b.bookingid
                ORDER BY r.roomnumber
                """;
        return databaseClient.sql(sql)
    .bind("dateStart", dateStart)
    .bind("dateEnd", dateEnd)
    .<RoomDetailDTO>map((row, metadata) -> {
            return RoomDetailDTO.builder()
            .roomId(row.get("roomid", Integer.class))
            .roomNumber(row.get("roomnumber", String.class))
            .roomName(row.get("roomname", String.class))
            .roomType(row.get("roomtypename", String.class))
            .status(row.get("status", String.class))
            .reservation(row.get("bookingid") != null ? ReservationDetailDTO.builder()
                .bookingId(row.get("bookingid", Integer.class))
                .checkIn(row.get("daybookinginit", LocalDateTime.class))
                .checkOut(row.get("daybookingend", LocalDateTime.class))
                .guest(GuestInfoDTO.builder()
                    .name(row.get("firstname", String.class) + " " + row.get("lastname", String.class))
                    .type(row.get("client_type", String.class))
                    .build())
                .capacity(RoomCapacityDTO.builder()
                    .adults(row.get("numberadults", Integer.class))
                    .children(row.get("numberchildren", Integer.class))
                    .babies(row.get("numberbabies", Integer.class))
                    .adultsExtra(row.get("numberadultsextra", Integer.class))
                    .adultsMayor(row.get("numberadultsmayor", Integer.class))
                    .total(row.get("total_adults", Integer.class) + row.get("total_children", Integer.class))
                    .build())
                .build() : null)
            .standby(StandbyInfoDTO.builder()
                .reservationTime(row.get("reservation_time", String.class))
                .standbyTime(row.get("standby_time", String.class))
                .build())
            .payment(row.get("payment_method") != null ? PaymentInfoDTO.builder()
                .method(row.get("payment_method", String.class))
                .hasFeeding(row.get("has_feeding", Boolean.class))
                .build() : null)
            .build();
        })
        .all();

    }

    @Override
    public Mono<Long> countAllRoomsFiltered(LocalDateTime dateStart, LocalDateTime dateEnd) {
        String sql = """
                 SELECT COUNT(1) as total
                 FROM (
                    SELECT DISTINCT r.roomid
                    FROM room r
                    LEFT JOIN roomtype rt ON rt.roomtypeid = r.roomtypeid
                    LEFT JOIN roomoffer ro ON ro.roomid = r.roomid
                    LEFT JOIN booking b ON b.roomofferid = ro.roomofferid
                        AND b.daybookinginit <= :dateStart
                        AND b.daybookingend >= :dateEnd
                    LEFT JOIN userclient uc ON uc.userclientid = b.userclientid
                    LEFT JOIN paymentbook pb ON pb.bookingid = b.bookingid
                    LEFT JOIN paymentmethod pm ON pm.paymentmethodid = pb.paymentmethodid
                    LEFT JOIN booking_feeding bf ON bf.bookingid = b.bookingid
                 ) t
                """;
        return databaseClient.sql(sql)
                .bind("dateStart", dateStart)
                .bind("dateEnd", dateEnd)
                .<Long>map((row, metadata) -> {
                    return row.get("total", Long.class);
                })
                .one();
    }
}
