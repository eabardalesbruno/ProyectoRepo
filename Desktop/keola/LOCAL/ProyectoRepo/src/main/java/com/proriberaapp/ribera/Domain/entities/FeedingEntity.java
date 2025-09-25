package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Table("feeding")
public class FeedingEntity {
    @Id
    private Integer id;

    @Column("feedingname")
    private String name;

    @Column("description")
    private String description;

    @Column("cost")
    private BigDecimal cost;

    @Column("state")
    private Integer state;

}
