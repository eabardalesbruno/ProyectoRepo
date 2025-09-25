package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.utils.TimeLima;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "fullday_rate")
public class FullDayRateEntity {

    @Id
    @Column("rate_id")
    private Integer rateId;

    @Column("created_at")
    private LocalDateTime createdAt = TimeLima.getLimaTime();

    @Column("title")
    private String title;

    @Column("price")
    private BigDecimal price;

    @Column("description")
    private String description;

    @Column("user_category")
    private String userCategory;

    @Column("rate_type")
    private List<String> rateType;

    @Column("rate_status")
    private Boolean rateStatus = true;
}