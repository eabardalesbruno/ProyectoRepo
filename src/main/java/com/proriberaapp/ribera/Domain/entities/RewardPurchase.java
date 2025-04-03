package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table("reward_purchase")
public class RewardPurchase {

    @Id
    private Long id;
    private Long userId;
    private Integer quantity;
    private Double totalAmount;
    private String transactionId;
    private String purchaseNumber;
    private String status;
    private LocalDateTime createdAt;
}