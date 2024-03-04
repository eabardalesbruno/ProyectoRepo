package com.proriberaapp.ribera.Domain.entities;

public class PartnerPointsEntity {
    private Integer partnerPointId;
    private Integer userId;
    private Integer partnerPoints;

    public PartnerPointsEntity() {
    }

    public Integer getPartnerPointId() {
        return partnerPointId;
    }

    public void setPartnerPointId(Integer partnerPointId) {
        this.partnerPointId = partnerPointId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPartnerPoints() {
        return partnerPoints;
    }

    public void setPartnerPoints(Integer partnerPoints) {
        this.partnerPoints = partnerPoints;
    }
}
