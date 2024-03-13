package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Domain.enums.StateRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Getter
@Setter
@Builder
@Table("room")
public class RoomEntity {
    @Id
    @Column("roomid")
    private Integer roomId;
    @Column("roomname")
    private String roomName;
    private String image;
    private String occupation;
    private Integer capacity;
    @Column("termsid")
    private Integer termsId;
    private Boolean wifi;
    private Integer beds;
    @Column("roomnumber")
    private String roomNumber;
    @Column("roomtype")
    private String roomType;
    private String info;
    private StateRoom state;

    public static RoomEntity from(RoomEntity room) {
        return RoomEntity.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .image(room.getImage())
                .occupation(room.getOccupation())
                .wifi(room.getWifi())
                .beds(room.getBeds())
                .info(room.getInfo())
                .build();
    }
}
