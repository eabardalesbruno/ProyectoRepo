package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("roomdetail")
public class RoomDetailEntity {
    private Integer roomDetailId;
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

