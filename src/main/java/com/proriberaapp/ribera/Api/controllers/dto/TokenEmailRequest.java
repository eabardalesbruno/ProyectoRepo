package com.proriberaapp.ribera.Api.controllers.dto;

public class TokenEmailRequest {
    private String email;
    private Integer bookingId;
    private Integer userClientId;

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getUserClientId() {
        return userClientId;
    }

    public void setUserClientId(Integer userClientId) {
        this.userClientId = userClientId;
    }
}
