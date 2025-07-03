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

@Table(name = "occupancy")
@Getter
@Setter
@Builder
public class OccupancyEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("rule_name")
    private String ruleName;
    @Column("description")
    private String description;
    @Column("cash_percentage")
    private BigDecimal cashPercentage;
    @Column("rewards_percentage")
    private BigDecimal rewardsPercentage;
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
