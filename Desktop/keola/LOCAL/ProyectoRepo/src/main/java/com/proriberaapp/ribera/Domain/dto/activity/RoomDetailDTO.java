package com.proriberaapp.ribera.Domain.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RoomDetailDTO {
    private Integer roomId;
    private String roomNumber;
    private String roomName;
    private String roomType;
    private String status;
    private ReservationDetailDTO reservation;
    private PaymentInfoDTO payment;
    private StandbyInfoDTO standby;
}
