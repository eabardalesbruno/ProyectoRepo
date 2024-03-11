package com.proriberaapp.ribera.Domain.entities;

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
    private String terms;
    private String wifi;
    private String beds;
    @Column("checkin")
    private Timestamp checkIn;
    @Column("checkout")
    private Timestamp checkOut;
    private BigDecimal price;
    private Integer points;
    private String info;

    public static RoomEntity from(RoomEntity room) {
        return RoomEntity.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .image(room.getImage())
                .occupation(room.getOccupation())
                .terms(room.getTerms())
                .wifi(room.getWifi())
                .beds(room.getBeds())
                .checkIn(room.getCheckIn())
                .checkOut(room.getCheckOut())
                .price(room.getPrice())
                .points(room.getPoints())
                .info(room.getInfo())
                .build();
    }
}
