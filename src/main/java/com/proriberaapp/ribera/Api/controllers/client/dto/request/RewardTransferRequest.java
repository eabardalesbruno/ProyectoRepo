package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RewardTransferRequest {
    private String fromInput;
    private String toInput;
    private Double amount;
    private String subCategory;
}