package com.proriberaapp.ribera.Domain.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySummaryDTO {
    private Integer totalPaid;
    private Integer totalCheckIn;
    private Integer totalCheckOut;
    private Integer totalReservations;
    private Integer totalNoShow;
    private Integer totalAvailables;
}
