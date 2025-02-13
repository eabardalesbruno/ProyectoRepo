package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Table("pointstransaction")
@Getter
@Setter
@Builder
public class PointTransactionEntity {
    @Id()
    private Integer id;
    private Integer userid;
    private Integer transactiontypeid;

}
