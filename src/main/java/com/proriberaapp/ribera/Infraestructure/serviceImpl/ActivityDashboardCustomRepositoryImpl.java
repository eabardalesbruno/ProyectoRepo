package com.proriberaapp.ribera.Infraestructure.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.dto.activity.RoomDetailDTO;
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
    public Flux<RoomDetailDTO> findAllRoomsPaginated(LocalDateTime dateStart, LocalDateTime dateEnd, int size,
            int offset) {
        String sql = """
                 SELECT
                    r.roomid,
                    r.roomnumber,
                    r.roomname,
                    rt.roomtypename,
                    rt.categoryname,
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
                LIMIT :size OFFSET :offset
                """;
        return databaseClient.sql(sql)
                .bind("dateStart", dateStart)
                .bind("dateEnd", dateEnd)
                .bind("size", size)
                .bind("offset", offset)
                .map((row, meta) -> {
                    return RoomDetailDTO.builder()
                            .roomId(row.get("rowid", Integer.class))
                            .roomNumber(row.get("roomnumber", String.class))
                            .roomName(row.get("roomname", String.class))
                            .roomType(row.get("categoryname", String.class))
                            .status(row.get("status", String.class))
                            .build();
                }).all();
    }

    @Override
    public Mono<Long> countAllRoomsFiltered(LocalDateTime dateStart, LocalDateTime dateEnd) {
        String sql = """
                 SELECT COUNT(DISTINCT r.roomid)
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
                """;
        return databaseClient.sql(sql)
                .bind("dateStart", dateStart)
                .bind("dateEnd", dateEnd)
                .map((row, meta) -> row.get(0, Long.class))
                .one();
    }
}
