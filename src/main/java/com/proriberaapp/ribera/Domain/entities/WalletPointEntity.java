package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("wallet_point")
public class WalletPointEntity extends Auditable{
    @Id
    private Integer id;

    @Column("userid")
    private Integer userId;

    private Double points;
}
