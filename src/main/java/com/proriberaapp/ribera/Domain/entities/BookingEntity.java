package com.proriberaapp.ribera.Domain.entities;

import java.math.BigDecimal;

public class BookingEntity {
    private Integer bookingId;
    private Integer userId;
    private Integer paymentMethodId;
    private Integer bookingStateId;
    private String roomType;
    private String roomName;
    private Integer capacity;
    private BigDecimal cost;
    private Integer riberaPoints;
    private Integer inResortsPoints;
    private String detail;
    private String amenities;
    private String services;
    private String image;

    public BookingEntity() {
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Integer getBookingStateId() {
        return bookingStateId;
    }

    public void setBookingStateId(Integer bookingStateId) {
        this.bookingStateId = bookingStateId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getRiberaPoints() {
        return riberaPoints;
    }

    public void setRiberaPoints(Integer riberaPoints) {
        this.riberaPoints = riberaPoints;
    }

    public Integer getInResortsPoints() {
        return inResortsPoints;
    }

    public void setInResortsPoints(Integer inResortsPoints) {
        this.inResortsPoints = inResortsPoints;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}