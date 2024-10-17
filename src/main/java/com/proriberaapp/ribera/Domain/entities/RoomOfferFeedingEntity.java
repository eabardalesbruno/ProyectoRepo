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
@Table("roomoffer_feeding")
public class RoomOfferFeedingEntity {

    @Id
    @Column("roomofferfeedingid")
    private Integer roomOfferFeedingId;

    @Column("roomofferid")
    private Integer roomOfferId;

    @Column("feedingid")
    private Integer feedingId;
}
