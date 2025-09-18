package com.proriberaapp.ribera.Infraestructure.repository.activity.mapper;

import java.time.LocalDateTime;
import io.r2dbc.spi.Row;
import com.proriberaapp.ribera.Domain.dto.activity.GuestInfoDTO;
import com.proriberaapp.ribera.Domain.dto.activity.PaymentInfoDTO;
import com.proriberaapp.ribera.Domain.dto.activity.ReservationDetailDTO;
import com.proriberaapp.ribera.Domain.dto.activity.RoomCapacityDTO;
import com.proriberaapp.ribera.Domain.dto.activity.RoomDetailDTO;
import com.proriberaapp.ribera.Domain.dto.activity.StandbyInfoDTO;

public class ActivityDashboardMapper {
    
    public static RoomDetailDTO mapToRoomDetail(Row row) {
        return RoomDetailDTO.builder()
            .roomId(row.get("roomid", Integer.class))
            .roomNumber(row.get("roomnumber", String.class))
            .roomName(row.get("roomname", String.class))
            .roomType(row.get("roomtypename", String.class))
            .status(getCaseStatus(row))
            .reservation(getReservationDetail(row))
            .payment(getPaymentInfo(row))
            .standby(getStandbyInfo(row))
            .build();
    }

    private static String getCaseStatus(Row row) {
        Integer bookingId = row.get("bookingid", Integer.class);
        if (bookingId == null) return "DISPONIBLE";
        
        Integer bookingStateId = row.get("bookingstateid", Integer.class);
        if (bookingStateId != null) {
            if (bookingStateId == 1) return "NO SHOW";
            if (bookingStateId == 3) return "RESERVADO";
        }
        
        Integer paymentStateId = row.get("paymentstateid", Integer.class);
        if (paymentStateId != null && paymentStateId == 2) return "PAGADO";
        
        return "OTRO";
    }

    private static ReservationDetailDTO getReservationDetail(Row row) {
        Integer bookingId = row.get("bookingid", Integer.class);
        if (bookingId == null) return null;

        return ReservationDetailDTO.builder()
            .bookingId(bookingId)
            .checkIn(row.get("daybookinginit", LocalDateTime.class).toString())
            .checkOut(row.get("daybookingend", LocalDateTime.class).toString())
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
            .build();
    }

    private static PaymentInfoDTO getPaymentInfo(Row row) {
        String paymentMethod = row.get("payment_method", String.class);
        if (paymentMethod == null) return null;

        return PaymentInfoDTO.builder()
            .method(paymentMethod)
            .hasFeeding(row.get("has_feeding", Boolean.class))
            .build();
    }

    private static StandbyInfoDTO getStandbyInfo(Row row) {
        return StandbyInfoDTO.builder()
            .reservationTime(row.get("reservation_time", String.class))
            .standbyTime(row.get("standby_time", String.class))
            .build();
    }
}
