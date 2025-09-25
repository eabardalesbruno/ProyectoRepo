package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

@Data
public class QuotationOfferDto {
    Integer room_offer_id;
    Float adult_cost;
    Float kid_cost;
    Float infant_cost;
    Float adult_mayor_cost;
    Float adult_extra_cost;
    Float adult_reward; 
    Float kid_reward; 
    Float adult_mayor_reward; 
    Float adult_extra_reward; 
}
