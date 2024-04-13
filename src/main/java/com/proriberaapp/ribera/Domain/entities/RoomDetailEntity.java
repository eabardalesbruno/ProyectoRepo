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
@Table("roomdetail")
public class RoomDetailEntity {
    @Id
    @Column("roomdetailid")
    private Integer roomDetailId;
    private String information;
    private String terms;
    private Integer bedrooms;
    @Column("squaremeters")
    private String squareMeters;
    @Column("oceanviewbalcony")
    private Boolean oceanViewBalcony;
    @Column("balconyoverlookingpool")
    private Boolean balconyOverlookingPool;

    public static RoomDetailEntity from(RoomDetailEntity roomDetailEntity) {
        return RoomDetailEntity.builder()
                .roomDetailId(roomDetailEntity.getRoomDetailId())
                .information(roomDetailEntity.getInformation())
                .terms(roomDetailEntity.getTerms())
                .bedrooms(roomDetailEntity.getBedrooms())
                .squareMeters(roomDetailEntity.getSquareMeters())
                .oceanViewBalcony(roomDetailEntity.getOceanViewBalcony())
                .balconyOverlookingPool(roomDetailEntity.getBalconyOverlookingPool())
                .build();
    }
}

