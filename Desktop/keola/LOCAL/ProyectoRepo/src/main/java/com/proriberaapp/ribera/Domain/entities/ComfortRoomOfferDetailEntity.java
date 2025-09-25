package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Table("comfortroomofferdetail")
public class ComfortRoomOfferDetailEntity implements Serializable {
    @Column("roomofferid")
    private Integer roomOfferId;
    @Column("comforttypeid")
    private Integer comfortTypeId;
    private Integer quantity;
}
