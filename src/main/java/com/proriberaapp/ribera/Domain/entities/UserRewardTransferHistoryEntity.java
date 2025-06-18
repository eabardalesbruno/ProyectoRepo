package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("user_reward_transfer_history")
public class UserRewardTransferHistoryEntity {
    @Id
    @Column("user_reward_transfer_id")
    private Integer user_reward_transfer_id;

    @Column("transfer_date")
    private LocalDateTime transferDate;

    @Column("from_user_id")
    private Integer fromUserId;

    @Column("to_user_id")
    private Integer toUserId;

    @Column("subcategory")
    private String subCategory;

    @Column("usd_rewards_transferred")
    private Double usdRewardsTransferred;

    @Column("usd_rewards_remaining")
    private Double usdRewardsRemaining;

    @Column("expiration_date")
    private LocalDate expirationDate;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("email")
    private String email;

    @Column("type")
    private String type;

    @Column("wallet_point_id")
    private Integer WalletPointId;
}
