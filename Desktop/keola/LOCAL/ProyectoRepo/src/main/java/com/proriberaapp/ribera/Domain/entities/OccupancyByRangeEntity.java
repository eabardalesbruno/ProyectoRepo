package com.proriberaapp.ribera.Domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "occupancy_by_ranges")
@Getter
@Setter
@Builder
public class OccupancyByRangeEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("has_date_range")
    private Boolean hasDateRange;
    @Column("range_from_date")
    private String rangeFromDate;
    @Column("range_to_date")
    private String rangeToDate;
    @Column("has_time_range")
    private Boolean hasTimeRange;
    @Column("range_from_hour")
    private Integer rangeFromHour;
    @Column("range_from_minute")
    private Integer rangeFromMinute;
    @Column("max_rewards_percentage")
    private BigDecimal maxRewardsPercentage;
    @Column("exception_rewards_percentage")
    private BigDecimal exceptionRewardsPercentage;
    @Column("new_max_rewards_percentage")
    private BigDecimal newMaxRewardsPercentage;
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
