package com.proriberaapp.ribera.Domain.dto.activity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ReservationDetailDTO {
    private Integer bookingId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private GuestInfoDTO guest;
    private RoomCapacityDTO capacity;
}
