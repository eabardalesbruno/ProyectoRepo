package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("exchange_rate")
public class ExchangeRateEntity {

    @Id
    @Column("exchange_rate_id")
    private int exchangeRateId;
    private double buys;
    private double sale;
    private LocalDateTime date;
    @Column("modification_date")
    private LocalDateTime modificationDate;
}
