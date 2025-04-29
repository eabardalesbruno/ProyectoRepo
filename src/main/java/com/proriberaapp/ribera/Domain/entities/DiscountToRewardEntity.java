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
@Table("discount_to_reward")
public class DiscountToRewardEntity {

    @Id
    @Column("id")
    private Integer id;
    @Column("name")
    private String name;
    @Column("discount_value")
    private Double discountValue;
    @Column("status")
    private Integer status;
}
