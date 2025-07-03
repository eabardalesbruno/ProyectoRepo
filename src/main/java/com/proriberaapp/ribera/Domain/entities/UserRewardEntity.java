package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Domain.enums.RewardType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import io.r2dbc.postgresql.codec.Json;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_reward")
public class UserRewardEntity {
    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    private Double points;

    private LocalDateTime date;

    @Column("type")
    private String type;

    @Column("status")
    private Integer status; // 1=activo, 0=inactivo

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column("booking_id")
    private Integer bookingId;
}