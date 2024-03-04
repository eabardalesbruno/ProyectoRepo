package com.proriberaapp.ribera.Domain.entities;

public class BookingStateEntity {
    private Integer bookingStateId;
    private String bookingStateName;

    public BookingStateEntity() {
    }

    public Integer getBookingStateId() {
        return bookingStateId;
    }

    public void setBookingStateId(Integer bookingStateId) {
        this.bookingStateId = bookingStateId;
    }

    public String getBookingStateName() {
        return bookingStateName;
    }

    public void setBookingStateName(String bookingStateName) {
        this.bookingStateName = bookingStateName;
    }
}
