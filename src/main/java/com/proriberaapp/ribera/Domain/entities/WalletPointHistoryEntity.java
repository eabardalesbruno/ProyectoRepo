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
public class WalletPointHistoryEntity {
    @Id
    private Integer id;

    @Column("userid")
    private Integer userId;

    private Double points;

    @Column("transaction_date")
    private LocalDateTime transactionDate;
}
