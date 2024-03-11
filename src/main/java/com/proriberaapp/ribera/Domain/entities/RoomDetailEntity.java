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
    @Column("roomid")
    private Integer roomId;
    private String information;

    public static RoomDetailEntity from(RoomDetailEntity roomDetailEntity) {
        return RoomDetailEntity.builder()
                .roomDetailId(roomDetailEntity.getRoomDetailId())
                .roomId(roomDetailEntity.getRoomId())
                .information(roomDetailEntity.getInformation())
                .build();
    }
}

