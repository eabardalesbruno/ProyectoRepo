package com.proriberaapp.ribera.Domain.entities;

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
@Table("wallet_point_history")
public class WalletPointHistoryEntity extends Auditable{
    @Id
    private Integer id;

    @Column("user_id")
    private Integer userId;

    @Column("points")
    private Double points;

    @Column("wallet_point_id")
    private Integer walletPointId;
}
