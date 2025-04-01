package com.proriberaapp.ribera.Api.controllers.client.dto;

import com.proriberaapp.ribera.Domain.entities.FullDayDetailEntity;
import com.proriberaapp.ribera.Domain.entities.FullDayFoodEntity;
import com.proriberaapp.ribera.Domain.entities.MembershipDetail;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class FullDayRequest {
    private Integer receptionistId;
    private Integer userPromoterId;
    private Integer userClientId;
    private String type;
    private Timestamp bookingDate;
    private List<FullDayDetailEntity> details;
    private List<FullDayFoodEntity> foods;
    private List<MembershipDetail> membershipDetails;
}
