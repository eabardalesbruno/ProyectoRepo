package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("roomofferroom")
public class RoomOfferRoomEntity {
    @Column("roomid")
    private Integer roomId;
    @Column("roomofferid")
    private Integer roomOfferId;
}
