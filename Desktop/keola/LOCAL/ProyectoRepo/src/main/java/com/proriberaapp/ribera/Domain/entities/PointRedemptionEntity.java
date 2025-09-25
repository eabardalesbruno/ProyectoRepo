package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table("pointsredemption")
public class PointRedemptionEntity {
    @Id()
    private Integer id;
    private Integer transactionid;
    private double points;
    private Integer paymentbookid;
}
