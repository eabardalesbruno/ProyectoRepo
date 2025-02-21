package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("pointstransfers")
@Getter
@Setter
@Builder
public class PointTransferEntity {
    @Id()
    private Integer id;
    private Integer transactionid;
    private Integer senderuserid;
    private Integer receiveruserid;
    private double pointsamount;
  
}
