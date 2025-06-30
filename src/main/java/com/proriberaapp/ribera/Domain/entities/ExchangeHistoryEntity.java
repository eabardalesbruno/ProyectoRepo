package com.proriberaapp.ribera.Domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table("exchange_history")
public class ExchangeHistoryEntity {
    @Id
    @Column("id")
    private Integer id;
    @Column("user_id")
    private Integer userId;
    @Column("username")
    private String username;
    @Column("exchange_date")
    private String exchangeDate;
    @Column("exchange_type")
    private String exchangeType;
    @Column("exchange_code")
    private String exchangeCode;
    @Column("service")
    private String service;
    @Column("description")
    private String description;
    @Column("check_in_date")
    private String checkInDate;
    @Column("check_out_date")
    private String checkOutDate;
    @Column("usd_rewards")
    private Double usdRewards;
    @Column("status")
    private Integer status;
    @Column("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
