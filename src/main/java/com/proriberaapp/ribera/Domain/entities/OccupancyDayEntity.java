package com.proriberaapp.ribera.Domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "occupancy_day")
@Getter
@Setter
@Builder
public class OccupancyDayEntity {
    @Column("occupancy_id")
    private Integer occupancyId;
    @Column("day_id")
    private Integer dayId;
    @Column("status")
    private Integer status;
    @Column("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    @Column("created_by")
    private String createdBy;
    @Column("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;
    @Column("updated_by")
    private String updatedBy;
}
