package com.proriberaapp.ribera.Domain.entities;

public class RoomDetailEntity {
    private Integer roomDetailId;
    private Integer roomId;
    private String information;

    public RoomDetailEntity() {
    }

    public Integer getRoomDetailId() {
        return roomDetailId;
    }

    public void setRoomDetailId(Integer roomDetailId) {
        this.roomDetailId = roomDetailId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}

