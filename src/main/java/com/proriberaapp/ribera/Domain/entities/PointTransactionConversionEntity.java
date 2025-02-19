package com.proriberaapp.ribera.Domain.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Table("pointtransactionconversion")
public class PointTransactionConversionEntity {
    @Id()
    private Integer id;
    private Integer userid;
    private double pointacredited;
    private double pointdebited;
    private double membershippoints;
    private Integer pointtypeid;
    private Integer transactionid;
    private String membershipname;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    
}
