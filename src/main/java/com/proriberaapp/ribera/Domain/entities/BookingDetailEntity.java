package com.proriberaapp.ribera.Domain.entities;
import java.time.LocalDateTime;

public class BookingDetailEntity {
    private Integer bookingDetailId;
    private Integer roomId;
    private Integer bookingId;
    private Integer paymentStateId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String roomNumber;
    private String roomType;
    private String roomName;
    private Integer capacity;
    private String customer;
    private String documentType;
    private String documentNumber;
    private Integer adultsNumber;
    private Integer childrenNumber;
    private Integer babiesNumber;

    public Integer getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(Integer bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getPaymentStateId() {
        return paymentStateId;
    }

    public void setPaymentStateId(Integer paymentStateId) {
        this.paymentStateId = paymentStateId;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Integer getAdultsNumber() {
        return adultsNumber;
    }

    public void setAdultsNumber(Integer adultsNumber) {
        this.adultsNumber = adultsNumber;
    }

    public Integer getChildrenNumber() {
        return childrenNumber;
    }

    public void setChildrenNumber(Integer childrenNumber) {
        this.childrenNumber = childrenNumber;
    }

    public Integer getBabiesNumber() {
        return babiesNumber;
    }

    public void setBabiesNumber(Integer babiesNumber) {
        this.babiesNumber = babiesNumber;
    }
}

