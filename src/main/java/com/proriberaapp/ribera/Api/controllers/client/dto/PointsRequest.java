package com.proriberaapp.ribera.Api.controllers.client.dto;

public class PointsRequest {
    private Integer partnerPointId;
    private Integer bookingId;

    // Getters and setters
    public Integer getPartnerPointId() {
        return partnerPointId;
    }

    public void setPartnerPointId(Integer partnerPointId) {
        this.partnerPointId = partnerPointId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
