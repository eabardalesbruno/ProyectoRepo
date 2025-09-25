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
@Table("room")
public class RoomEntity {
    @Id
    @Column("roomid")
    private Integer roomId;
    @Column("roomtypeid")
    private Integer roomTypeId;
    @Column("stateroomid")
    private Integer stateRoomId;
    @Column("roomdetailid")
    private Integer roomDetailId;
    @Column("roomdescription")
    private String roomDescription;
    @Column("roomname")
    private String roomName;
    @Column("roomnumber")
    private String roomNumber;
    private String image;
    @Column("adultcapacity")
    private Integer adultCapacity;
    @Column("adultextra")
    private Integer adultExtra;
    @Column("kidcapacity")
    private Integer kidCapacity;
    @Column("adultmayorcapacity")
    private Integer adultMayorCapacity;
    @Column("infantcapacity")
    private Integer infantCapacity;

    public static RoomEntity from(RoomEntity room) {
        return RoomEntity.builder()
                .roomId(room.getRoomId())
                .roomTypeId(room.getRoomTypeId())
                .stateRoomId(room.getStateRoomId())
                .roomDetailId(room.getRoomDetailId())
                .roomName(room.getRoomName())
                .roomNumber(room.getRoomNumber())
                .image(room.getImage())
                .adultCapacity(room.getAdultCapacity())
                .adultExtra(room.getAdultExtra())
                .kidCapacity(room.getKidCapacity())
                .adultMayorCapacity(room.getAdultMayorCapacity())
                .infantCapacity(room.getInfantCapacity())
                .build();
    }
}
