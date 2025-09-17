package com.proriberaapp.ribera.Domain.dto.activity.response;

import java.time.LocalDateTime;
import java.util.List;

import com.proriberaapp.ribera.Domain.dto.activity.ActivitySummaryDTO;
import com.proriberaapp.ribera.Domain.dto.activity.PaginationDTO;
import com.proriberaapp.ribera.Domain.dto.activity.RoomDetailDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ActivityDashboardResponseDTO {
    private Boolean success;
    private ActivitySummaryDTO data;
    private List<RoomDetailDTO> rooms;
    private PaginationDTO pagination;
    private LocalDateTime timestamp;

    /*
     * @Data
     * 
     * @Builder
     * 
     * @NoArgsConstructor
     * 
     * @AllArgsConstructor
     * public static class ActivityDataDTO {
     * private ActivitySummaryDTO summary;
     * private List<RoomDetailDTO> rooms;
     * private PaginationDTO pagination;
     * }
     */
}
