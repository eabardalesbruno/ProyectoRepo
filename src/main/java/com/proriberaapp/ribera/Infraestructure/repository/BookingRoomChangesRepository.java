package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.bookingroomchanges.response.BookingRoomChangeEmailResponseDto;
import com.proriberaapp.ribera.Domain.entities.BookingRoomChangesEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface BookingRoomChangesRepository extends R2dbcRepository<BookingRoomChangesEntity,Integer> {

    @Query(value = """
            SELECT
                rn.image AS imgurlnew,
                CONCAT(uc.firstname, ' ', uc.lastname) AS clientname,
                uc.email AS clientemail,
                ro.roomname AS roomnameold,
                rn.roomname AS roomnamenew,
                b.bookingid,
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
                        WHEN 1 THEN 'Ene'
                        WHEN 2 THEN 'Feb'
                        WHEN 3 THEN 'Mar'
                        WHEN 4 THEN 'Abr'
                        WHEN 5 THEN 'May'
                        WHEN 6 THEN 'Jun'
                        WHEN 7 THEN 'Jul'
                        WHEN 8 THEN 'Ago'
                        WHEN 9 THEN 'Set'
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
                        WHEN 1 THEN 'Ene'
                        WHEN 2 THEN 'Feb'
                        WHEN 3 THEN 'Mar'
                        WHEN 4 THEN 'Abr'
                        WHEN 5 THEN 'May'
                        WHEN 6 THEN 'Jun'
                        WHEN 7 THEN 'Jul'
                        WHEN 8 THEN 'Ago'
                        WHEN 9 THEN 'Set'
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
                ) AS totalpeople
            FROM booking_room_changes brc
            INNER JOIN booking b ON b.bookingid = brc.booking_id
            INNER JOIN roomoffer ron ON ron.roomofferid = brc.new_room_offer_id
            INNER JOIN room rn ON rn.roomid = ron.roomid
            INNER JOIN roomoffer roo ON roo.roomofferid = brc.old_room_offer_id
            INNER JOIN room ro ON ro.roomid = roo.roomid
            INNER JOIN userclient uc ON b.userclientid = uc.userclientid
            WHERE brc.booking_id = :bookingId AND brc.room_change_id = :roomChangeId;
            """)
    Mono<BookingRoomChangeEmailResponseDto>getDataForEmailRoomChange(Integer bookingId,Integer roomChangeId);
}
